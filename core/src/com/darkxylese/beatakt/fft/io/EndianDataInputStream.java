package com.darkxylese.beatakt.fft.io;

import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EndianDataInputStream extends DataInputStream
{	
	public EndianDataInputStream(InputStream in)
	{
		super(in);		
	}

	public String read4ByteString( ) throws Exception
	{
		byte[] bytes = new byte[4];
		readFully(bytes);
		return new String( bytes, StandardCharsets.US_ASCII);
	}
	
	public short readShortLittleEndian( ) throws Exception
	{
		int result = readUnsignedByte();
		result |= readUnsignedByte() << 8;		
		return (short)result;		
	}
	
	public int readIntLittleEndian( ) throws Exception
	{
		int result = readUnsignedByte();
		result |= readUnsignedByte() << 8;
		result |= readUnsignedByte() << 16;
		result |= readUnsignedByte() << 24;
		return result;		
	}

}
