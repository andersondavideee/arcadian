package org.steelthread.arcadian.service;

import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.security.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.steelthread.arcadian.network.DiceConnectionImpl;


public class Console

{
  private static final Logger logger = LoggerFactory.getLogger(Console.class);

  public static void main(String args[])
  {
    try
    {
      Console console = new Console();
      console.start(args);
    }
    catch(Exception e)
    {
      logger.debug(e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void start(String args[]) throws Exception 
  {
     Connection conn =  null ;
     if ((conn = make_connection(args)) == null)
     {
       System.err.println("unable to complete connection");
       return;
     }

     command_loop(conn);
  }


  public Connection make_connection(String args[]) 
  {
    Connection conn = new Connection();

    /*
    //verify minimum number of arguments
    if (args.length < 4)
    {
      System.err.println("usage: java "+ this.getClass().getName() + " <host> <port> <user> <pass> [true|false]");
      System.exit(1);
    }

    //process the arguments
    String host = args[0];
    String user = args[2];
    String pass = args[3];
    Integer port = 0;
    try
    {
      port = Integer.parseInt(args[1]);
    }
        catch(Exception e)
    {
      System.err.println(e.getMessage());
      System.err.println("invalid port number");
      return null;
    } 
    */
    //process the arguments
    String host = "207.210.252.116";
    String user = "";
    String pass = "xega9usp";
    Integer port = 47200;

    //connect to the server
    logger.debug("connecting to "+host+":"+port+ " ...");
    try
    {
      conn.connect(host, port);
    }
    catch(Exception e)
    {
      System.err.println(e.getMessage());
      System.err.println("error connecting to "+host+":"+port);
      return null;
    }
    logger.debug("connection established");
  
    //authenticate on the server 
    logger.debug("authenticating as " + user+":"+pass+" ...");
    try
    {
      conn.authenticate(user, pass);
    }
    catch(Exception e)
    {
      System.err.println(e.getMessage());
      System.err.println("error authenticating at "+host+":"+port);
      return null;
    }
  
    if (args.length > 4 && args[4].equals("true"))
      conn.set_debug(true);

    logger.debug("authentication successful");

    return conn;
  }

  public void command_loop(Connection conn) throws Exception
  {
    logger.debug(" |");
    logger.debug(" | Welcome to the Java Battlefield 3 command console");
    logger.debug(" | by Miguel Mendoza <miguel@micovery.com>");
    logger.debug(" | Examples:");
    logger.debug(" |          admin.listPlayers all");
    logger.debug(" |          admin.say \"Hello World\" all");
    logger.debug(" |          admin.help");

    List<String> command = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    // Start receiving events 
    // Comment this out if you want to just input commands
    conn.send(Arrays.asList(new String[] { "admin.eventsEnabled", "true" }));

    // This allows us to sit and receive commands from the console
    // Comment this out if you want to just input commands
    while(true)
    {
       List<String> response = conn.recv();
   
       if(false) {
         break;
       }
       /* make sure response is valid */ 
       if (response == null || response.size() == 1)
       {
         System.err.println("error while receiving server response");
         continue;
       }
       /* first element is serquence number, ignore it */
       response.remove(0);

       /* print the server response */
       logger.debug("server response:");
       for(String word : response) {
         logger.debug(word+" ");
       }
    }

    while(true)
    {
       System.out.print(">>> ");

       /* read the command */
       if ((command = next_command(in)) == null)
         break;

       /* make sure command is valid */
       if (command.size() == 0)
         continue;

       /* send the command to the server */
       conn.send(command);
       List<String> response = conn.recv();
   
       /* make sure response is valid */ 
       if (response == null || response.size() == 1)
       {
         System.err.println("error while receiving server response");
         continue;
       }
       /* first element is serquence number, ignore it */
       response.remove(0);

       /* print the server response */
       logger.debug("server response:");
       for(String word : response)
         logger.debug(word+",");
       logger.debug("");
    }
  }

  /* simple command line parser */
  public List<String> next_command(BufferedReader in) throws Exception
  {
    /* simple parser for command line */
    boolean inside_string = false;
    boolean previous_space = false;
    boolean escape_char = false;

    String word = "";
    char c = (char) 0;
    int data = -1;

    List<String> words = new Vector<String>();

    while((data = in.read()) != -1)
    {
       c = (char) data;

       /* escaping quotes inside string */
       if (escape_char == true && c == (char) '"' && inside_string == true)
       {
         word += Character.toString(c);
         escape_char = false;
         continue;
       }
       /* escaping the escape character anywhere */
       else if(escape_char == true && c == (char) '\\')
       {
         word += Character.toString(c);
         escape_char = false;
         continue;
       }
       /* handle line continuation */
       else if (escape_char == true && c == (char) '\n')
       {
         escape_char = false;
         continue;
       }
       else if(escape_char == true && c == (char) 't')
       {
         word += Character.toString('\t');
         escape_char = false;
         continue;
       }
       else if(escape_char == true)
       {
         /* finish readling the line */
         in.readLine();
         System.err.println("input error: unknown escape sequence \\"+Character.toString(c));
         return new Vector<String>();
       }
       /* detect start of string */
       else if (c == (char) '"'  && inside_string == false)
        inside_string = true;
       /* detect end of string */
       else if(c == (char) '"' && inside_string == true)
         inside_string = false;
       /* detect escape character */
       else if (c == (char) '\\')
         escape_char = true;
       /* detect unterminated stirng literal */
       else if (c == (char) '\n' && inside_string == true)
       {
         System.err.println("input error: unterminated string literal"); 
         return new Vector<String>();
       }
       /* skip white-space */
       else if(inside_string == false && previous_space == true && 
               (c == (char) ' ' || c == (char) '\t'))
          continue;
       /* detect end of word */
       else if (inside_string == false &&
               (c == (char) ' ' || c == (char) '\t' ||
                c == (char) '\n' || c == (char) '\r'))       
       {
         previous_space = true;
         word = word.trim();
         if (word.length() > 0)
           logger.debug("adding word:" + word);
            words.add(word);
         word = "";
 
         if (c == (char) '\n')
            return words;
       }
       else
       {
         word += Character.toString(c);
         previous_space = false;
         escape_char = false; /* fail-safe */
       }
       
     }
     return null;
  }

  /* Helper classes below */

  /* Start of Connection class */
  private class Connection
  {
    private int global_sequence = 0;
    private Socket sock = null;
    private InputStream in = null;
    private OutputStream out = null;
    private Boolean authenticated = false;
  
    private int port;
    private String host;
    private String user;
    private String pass;
  
    private byte[] BUF;

    private boolean debug = true;
  
    public Connection()
    {
      BUF = new byte[Packet.MAX_PACKET_SZ];
    }
  
  
    /* read packet to completion */
    private int read_packet(byte buf[]) throws Exception
    {
      int read = 0;
      /* this is more efficient than allocating a new buffer */
      Arrays.fill(buf, (byte) 0); 
  
      int packet_size = 0;
      Boolean complete = false;
      while(read < buf.length)
       {
         buf[read++] = (byte) in.read();
  
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
        throw new Exception("incomplete packet received");
  
      return packet_size;
    }
  
  
    public int send(List<String> words) throws Exception
    {
      /*  logger.debug(" == Sending Packet == "); */ 
  
      Packet packet = new Packet(words, Packet.Type.Request,  Packet.Origin.Client, ++global_sequence);
  
      byte[] buffer =  packet.getBytes();

      if (debug)  
      {
        logger.debug("SEND::");
        Packet.dump(buffer, buffer.length); 
      }
      
      out.write(buffer);
      return packet.getSequence();
  
    }
  
    public List<String> recv() throws Exception
    {
      logger.debug("== Waiting for Packet =="); 
      int size = read_packet(BUF);

      if (debug)  
      {
        logger.debug("RECV::");
        Packet.dump(BUF, size); 
      }

      logger.debug("== Received Packet(" + size + ") ==");
      Packet packet = new Packet(BUF, size);
  
      return packet.getWords();
    }
  
  
    public Boolean connect(String host, Integer port) throws Exception
    {
        /* logger.debug("connecting to "+fhost+":"+fport); */
        sock = new Socket(host, port);
        /* logger.debug("connecting established"); */
  
        if (sock == null)
          throw new Exception("cannot create socket");
  
        /* get the input and output streams */
        in =  sock.getInputStream();
        out = sock.getOutputStream();
  
        this.host = host;
        this.port = port;
  
        return true;
    }
  
  
    public Boolean authenticate(String user, String password) throws Exception
    {
  
      /*
      if (!(user == null || user.equals("admin")))
        throw new Exception("PRoCon layer connections not supported");
      */
      /* direct connection to server */
  
      List<String> words = new Vector<String>();
      words.add("login.hashed");
    
      /* send the hash login request */ 
      send(words);
      List<String> rwords = recv();
      if (rwords.size() != 3 || !rwords.get(1).equals("OK"))
        throw new Exception("invalid server response");
  
      /* build the salted md5 password hash */ 
      String salt_hex = rwords.get(2);
      byte [] salt_bin = decode_hex(salt_hex);
  
      MessageDigest dg =  MessageDigest.getInstance("MD5");
      dg.update(decode_hex(salt_hex));
      dg.update(password.getBytes());
  
      byte[] md5_bin = dg.digest();
      String md5_hex = encode_hex(md5_bin);
  
  
      /* send the salted md5 password hash */
      words = new Vector<String>();
      words.add("login.hashed");
      words.add(md5_hex);
  
      send(words);
      rwords = recv();
      if (rwords.size() != 2 || !rwords.get(1).equals("OK"))
        throw new Exception("invalid username or password");
  
  
      authenticated = true;
  
      this.user = user;
      this.pass = password;
  
      return true;
    }
  
  
  
    private byte[] decode_hex(String str)
    {
      byte result[] = new byte[str.length()/2];
      for (int i=0, j=0; i < str.length();  i+=2, j++)
         result[j] = Integer.valueOf(str.substring(i, i+2), 16).byteValue();
  
      return result;
    }
  
    private String encode_hex(byte[] data)
    {
       String str = "";
       for(int i=0; i < data.length; i++)
         str += String.format("%02X", data[i]);
  
       return str;
    }

    public void set_debug(boolean dbg)
    {
      debug = dbg;
    }
  
  }
  /* End of Connection class */

 
  /* Start of Packet class */
  private static class Packet
  {
  
    static final int MAX_PACKET_SZ = 16384;
  
    static enum Type {Response, Request};
    static enum Origin {Server, Client};
  
    int sequence;
    private Type type;
    private Origin origin;
    private List<String> words;
    
    public Packet()
    {
      this.init(null, null, null, 0);
    }
  
    public Packet(Type type, Origin origin, int sequence)
    {
      this.init(null, type, origin, sequence);
    }
  
    public Packet(Type type, Origin origin)
    {
      this.init(null, type, origin, 0);
    }
   
    public Packet(byte buffer[], int size) throws Exception
    {
      this.init(null, null, null, 0);
      decode_packet(this, buffer, size);
    }
  
    public static byte[] encode_packet(Packet pkt)
    {
      // calculate total packet size in bytes 
      int size = 0;
      size += 4; // 4 bytes for sequence
      size += 4; // 4 bytes for size
      size += 4; // 4 bytes for number of words
  
      for(String word : pkt.getWords())
        size += 4 + word.length() + 1; // 4 -word length, 1 - Null Termniator
  
      /* logger.debug("PK SZ: "+ size); */
      byte buffer[] = new byte[size];
  
      ByteBuffer buf = ByteBuffer.wrap(buffer);
      buf.order(ByteOrder.LITTLE_ENDIAN);
  
      int sequence = pkt.getSequence();
      logger.debug("PK SEQ: "+ sequence); 
  
      Type type = pkt.getType();
  
      if (type.equals(Type.Response))
        sequence = sequence | 0x40000000;
  
      logger.debug("PK TYPE: " + type.toString());
      
      Origin origin = pkt.getOrigin();
  
      if (origin.equals(Origin.Client))
        sequence = sequence | 0x80000000;
  
      logger.debug("PK ORG: " + origin.toString());
  
      // put the combined sequence, origin, and type
      buf.putInt(sequence);
  
      // put the packet size
      buf.putInt(size);
  
      // put the number of words
      List<String> words = pkt.getWords();
      int nwords = words.size();
      buf.putInt(nwords);
  
      /* logger.debug("OK WRDS: "+ nwords); */ 
  
      // put one word at a time
      for (int i = 0; i < nwords; i++)
      {
        String word = words.get(i);
        /* logger.debug("PK WRD: "+ word); */
      
        // put the word length
        buf.putInt(word.length());
  
        //put the word itself
        buf.put(word.getBytes());
  
        //put the null terminator
        buf.put((byte) 0);
      }
       
      return buffer;
    }
  
    public static void decode_packet(Packet pkt, byte buffer[], int size) throws Exception
    {
  
      ByteBuffer buf = ByteBuffer.wrap(buffer,0, size);
  
  
      buf.order(ByteOrder.LITTLE_ENDIAN);
      int sequence = buf.getInt();
  
      /* extract the origin bit */
      Origin origin;
      if ((sequence & 0x80000000) == 0x80000000)
        origin = Origin.Client;
      else
        origin = Origin.Server;
  
      pkt.setOrigin(origin);
  
      logger.debug("PK ORG: " + origin.toString());
  
      /* extract the type bit */ 
      Type type;
      if ((sequence & 0x40000000) == 0x40000000)
        type = Type.Response;
      else
        type = Type.Request;
  
      pkt.setType(type);
  
      logger.debug("PK TYPE: " + type.toString());
  
      /* mask the sequence number */
      sequence = sequence & 0x3FFFFFFF;
      pkt.setSequence(sequence);
  
      logger.debug("PK SEQ: " +  sequence);
  
      int sz = buf.getInt();
      if (sz != size)
        throw new Exception("size in buffer(" + sz + ") does not match given size(" + size + ") argument");
  
      /* logger.debug("PK SIZE: " + size); */
  
      int nwords = buf.getInt();
      /* logger.debug("PK WRDS: " + nwords); */
  
  
      pkt.addWord(new Integer(sequence).toString());
  
      while(nwords-- > 0)
      {
        /* get the word size */
        int word_sz = buf.getInt();
  
        /* allocate word buffer */ 
        byte[] text = new byte[word_sz];
  
        /* get the word */
        buf.get(text);
  
        /* skip the null terminator */
        buf.get(); 
        pkt.addWord(text);
        /* logger.debug("PK WRD: " + new String(text)); */
      }
  
    }
  
    public Packet(List<String> words, Type type, Origin origin, int sequence)
    {
      this.init(words, type, origin, sequence);
    }
  
  
    public Packet(List<String> words, Type type, Origin origin)
    {
      this.init(words, type, origin, 0);
    }
  
  
    private void init(List<String> words, Type type, Origin origin, int sequence)
    {
      if (words == null)
        this.setWords(new Vector<String>());
      else
        this.setWords(words);
  
      if (type == null)
        this.setType(Packet.Type.Request);
      else
        this.setType(type);
      
      if (origin == null)
        this.setOrigin(Packet.Origin.Client);
      else
        this.setOrigin(origin);
  
      this.setSequence(sequence);
    }
  
    /* Accessors & Mutators */
  
    public byte[] getBytes()
    {
      return encode_packet(this);
    }
    
    public List<String> getWords()
    {
      return words;
    }
  
    public void setWords(List<String> words)
    {
      this.words = words;
    }
    
    public void addWord(String text)
    {
      this.words.add(new String(text));
    }
  
    public void addWord(byte text[])
    {
      this.words.add(new String(text));
    }
    
    public Type getType()
    {
      return type;
    }
  
    public void setType(Type type)
    {
      this.type = type;
    }
  
    public Origin getOrigin()
    {
      return origin;
    }
  
    public void setOrigin(Origin origin)
    {
      this.origin = origin;
    }
  
    public int getSequence()
    {
      return sequence;
    }
  
    public void setSequence(int sequence)
    {
      this.sequence = sequence;
    }

    public static void dump(byte[] buffer, int size)
    {
       if (size > buffer.length)
        size = buffer.length;
 
       int max = ((int) Math.ceil((float) size / 16.0))* 16;
       System.out.print("           ");
       for(int i=0; i <= max; i++)
       {
         if (i % 8 == 0 && i != 0)
           System.out.print("");
 
         if (i % 16 == 0 && i != 0)
           {
              System.out.print(": ");
              for(int j=i-16; j < i; j++)
                 if (j < size &&  (char) buffer[j] >= 32 && (char) buffer[j] < 127)
                   System.out.printf("%c",(char) buffer[j]);
                 else if (j < size)
                   System.out.printf(".");
                 else
                   System.out.printf(" ");
 
              if (i < max )
                System.out.print("\n           ");
              else
                System.out.print("\n");
           }
 
         if (i < size)
           System.out.printf("%02X ", buffer[i]);
         else if (i < max)
           System.out.printf("   ");
       }
    }
  }
 /* End of Packet class */

  
} 
