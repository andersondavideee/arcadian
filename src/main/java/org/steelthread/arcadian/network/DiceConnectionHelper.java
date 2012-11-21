package org.steelthread.arcadian.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiceConnectionHelper {

  private byte[] BUF = new byte[Packet.MAX_PACKET_SZ];
  private int global_sequence = 0;
  private Socket sock = null;
  private InputStream in = null;
  private OutputStream out = null;  
  private static final Logger logger = LoggerFactory.getLogger(DiceConnectionHelper.class);
  private boolean debug = true;
  
  protected synchronized List<String> sendCommand(List<String> commands) {
    Packet packet = new Packet(commands, Packet.Type.Request,  Packet.Origin.Client, ++global_sequence);
    byte[] buffer =  packet.getBytes();

    if (debug)  
    {
      logger.debug("SEND::");
      Packet.dump(buffer, buffer.length); 
    }
    
    try {
      out.write(buffer);
    } catch (IOException e) {
     throw new ConnectionException(e);
    }
    return recv();
  }
  
  public List<String> recv() {
    /* logger.debug("== Waiting for Packet =="); */ 
    int size = read_packet(BUF);

    if (debug)  
    {
      //logger.debug("RECV::");
      //Packet.dump(BUF, size); 
    }

    Packet packet = new Packet(BUF, size);
    /* print the server response */
    List<String> response = packet.getWords();
    if (debug) {
      StringBuilder stringBuilder = new StringBuilder();
      for(String word : response) {
        stringBuilder.append(word+ ",");
      }
      logger.debug(stringBuilder.toString());
    }
    return response;
  }  
  
  /* read packet to completion */
  private int read_packet(byte[] buf) {
    int read = 0;
    /* this is more efficient than allocating a new buffer */
    Arrays.fill(buf, (byte) 0); 

    int packet_size = 0;
    Boolean complete = false;
    while(read < buf.length)
     {
       try {
        buf[read++] = (byte) in.read();
      } catch (IOException e) {
        throw new ConnectionException(e);
      }

       if (read < 8)
         continue;
       else if (read == 8)
       {
          packet_size = ByteBuffer.wrap(new byte[]{buf[7], buf[6], buf[5], buf[4]}).getInt();
          /* logger.debug("PACKET SIZE: " + packet_size); */
       }

       if (read == packet_size)
       {
         complete = true;
         break;
       }
     }

    if (complete == false)
      throw new ConnectionException("incomplete packet received");

    return packet_size;
  }  
  
  public Boolean connect(String host, Integer port) {
      try {
        sock = new Socket(host, port);
      } catch (UnknownHostException e) {
      } catch (IOException e) {
        throw new ConnectionException(e);
      }

      if (sock == null)
        throw new ConnectionException("cannot create socket");

      /* get the input and output streams */
      try {
        in =  sock.getInputStream();
        out = sock.getOutputStream();
      } catch (IOException e) {
        throw new ConnectionException(e);
      }
      return true;
  }  
  
  public void close() {
    try {
      sock.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public Boolean authenticate(String user, String password) {
    List<String> words = new Vector<String>();
    words.add("login.hashed");
  
    /* send the hash login request */ 
    List<String> rwords = sendCommand(words);
    if (rwords.size() != 3 || !rwords.get(1).equals("OK"))
      throw new ConnectionException("invalid server response");

    /* build the salted md5 password hash */ 
    String salt_hex = rwords.get(2);
    byte [] salt_bin = decode_hex(salt_hex);

    MessageDigest dg;
    try {
      dg = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new ConnectionException(e);
    }
    dg.update(decode_hex(salt_hex));
    dg.update(password.getBytes());

    byte[] md5_bin = dg.digest();
    String md5_hex = encode_hex(md5_bin);


    /* send the salted md5 password hash */
    words = new Vector<String>();
    words.add("login.hashed");
    words.add(md5_hex);

    rwords = sendCommand(words);
    if (rwords.size() != 2 || !rwords.get(1).equals("OK")) {
      throw new ConnectionException("invalid username or password");      
    }
    return true;
  }

  private byte[] decode_hex(String str)
  {
    byte result[] = new byte[str.length()/2];
    for (int i=0, j=0; i < str.length();  i+=2, j++)
       result[j] = Integer.valueOf(str.substring(i, i+2), 16).byteValue();

    return result;
  }

  private String encode_hex(byte[] data) {
    String str = "";
    for (int i = 0; i < data.length; i++)
      str += String.format("%02X", data[i]);

    return str;
  }
  
}