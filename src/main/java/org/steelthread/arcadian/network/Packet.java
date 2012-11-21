package org.steelthread.arcadian.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Packet {
  
  private static final Logger logger = LoggerFactory.getLogger(Packet.class);
  
   protected final static int MAX_PACKET_SZ = 16384;
   protected enum Type {Response, Request};
   protected enum Origin {Server, Client};

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
 
  public Packet(byte buffer[], int size) {
    this.init(null, null, null, 0);
    decode_packet(this, buffer, size);
  }

  public  byte[] encode_packet(Packet pkt)
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
    /* logger.debug("PK SEQ: "+ sequence); */ 

    Type type = pkt.getType();

    if (type.equals(Type.Response))
      sequence = sequence | 0x40000000;

    /* logger.debug("PK TYPE: " + type.toString()); */
    
    Origin origin = pkt.getOrigin();

    if (origin.equals(Origin.Client))
      sequence = sequence | 0x80000000;

    /* logger.debug("PK ORG: " + origin.toString()); */

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

  public  void decode_packet(Packet pkt, byte buffer[], int size) {

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

    /* logger.debug("PK ORG: " + origin.toString()); */

    /* extract the type bit */ 
    Type type;
    if ((sequence & 0x40000000) == 0x40000000)
      type = Type.Response;
    else
      type = Type.Request;

    pkt.setType(type);

    /* logger.debug("PK TYPE: " + type.toString()); */

    /* mask the sequence number */
    sequence = sequence & 0x3FFFFFFF;
    pkt.setSequence(sequence);

    /* logger.debug("PK SEQ: " +  sequence); */

    int sz = buf.getInt();
    if (sz != size)
      throw new RuntimeException("size in buffer(" + sz + ") does not match given size(" + size + ") argument");

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
     logger.debug("           ");
     for(int i=0; i <= max; i++)
     {
       if (i % 8 == 0 && i != 0)
         logger.debug("");

       if (i % 16 == 0 && i != 0)
         {
         logger.debug(": ");
            for(int j=i-16; j < i; j++)
               if (j < size &&  (char) buffer[j] >= 32 && (char) buffer[j] < 127)
                 logger.debug("%c",(char) buffer[j]);
               else if (j < size)
                 logger.debug(".");
               else
                 logger.debug(" ");

            if (i < max )
              logger.debug("\n           ");
            else
              logger.debug("\n");
         }

       if (i < size)
         logger.debug("%02X ", buffer[i]);
       else if (i < max)
         logger.debug("   ");
     }
  }
}