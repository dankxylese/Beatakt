package com.darkxylese.beatakt.fft;

import com.darkxylese.beatakt.fft.analysis.SpectrumProvider;
import com.darkxylese.beatakt.fft.analysis.ThresholdFunction;
import com.darkxylese.beatakt.fft.io.MP3Decoder;
import com.darkxylese.beatakt.fft.visualization.PlaybackVisualizer;
import com.darkxylese.beatakt.fft.visualization.Plot;


import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MultiBandThreshold
{

	// TODO: SpectralFlux -> SignalFluctuations
	public static final String FILE = "/home/manse/Beatakt/Songs/DiscoFries.mp3";
	public static final int HOP_SIZE = 512;
	public static final int HISTORY_SIZE = 100;
	public static final float[] multipliers = { 2f, 2f, 2f };
	public static final float[] bands = { 80, 4000, 4000, 10000, 10000, 16000 };
	
	public static void main( String[] argv ) throws Exception
	{		
		MP3Decoder decoder = new MP3Decoder( new FileInputStream( FILE  ) );
		SpectrumProvider spectrumProvider = new SpectrumProvider( decoder, 1024, HOP_SIZE, true );
		float[] spectrum = spectrumProvider.nextSpectrum();
		float[] lastSpectrum = new float[spectrum.length];		
		List<List<Float>> spectralFlux = new ArrayList<List<Float>>( );
		for( int i = 0; i < bands.length / 2; i++ )
			spectralFlux.add( new ArrayList<Float>( ) );
		//makes half the bands number of empty (arrays[1]), as filler data inside the (array of arrays[2])
		//6 bands = 3 arrays[1]

		do
		{						
			for( int i = 0; i < bands.length; i+=2 )
			{				
				int startFreq = spectrumProvider.getFFT().freqToIndex( bands[i] );
				int endFreq = spectrumProvider.getFFT().freqToIndex( bands[i+1] );
				float flux = 0;
				for( int j = startFreq; j <= endFreq; j++ )
				{
					float value = (spectrum[j] - lastSpectrum[j]);
					value = (value + Math.abs(value))/2;
					flux += value;
				}
				spectralFlux.get(i/2).add( flux );
			}
					
			System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length );


		}
		while( (spectrum = spectrumProvider.nextSpectrum() ) != null );




		List<List<Float>> thresholds = new ArrayList<List<Float>>( );
		for( int i = 0; i < bands.length / 2; i++ )
		{
			List<Float> threshold = new ThresholdFunction( HISTORY_SIZE, multipliers[i] ).calculate( spectralFlux.get(i) );
			thresholds.add( threshold );
		}

		//write to file
		outputFile(spectralFlux, thresholds, decoder.getDuration());


		Plot plot = new Plot( "SignalFluctuations", 1024, 512 );
		for( int i = 0; i < bands.length / 2; i++ )
		{
			plot.plot( spectralFlux.get(i), 1, -0.6f * (bands.length / 2 - 2) + i, false, Color.red );
			plot.plot( thresholds.get(i), 1, -0.6f * (bands.length / 2 - 2) + i, true, Color.green );
		}
		
		new PlaybackVisualizer( plot, HOP_SIZE, new MP3Decoder( new FileInputStream( FILE ) ) );


	}

	static public void outputFile(List<List<Float>> spectralFlux, List<List<Float>> thresholds, int duration){
		try{
		RandomAccessFile stream = new RandomAccessFile("/home/manse/Beatakt/DiscoFries.bm", "rw");
		stream.setLength(0); //clear file before hand
		FileChannel channel = stream.getChannel();
		int k = 0;

		for( int i = 0; i < bands.length; i+=2 ){ //for each band
			for (int j = 0; j < spectralFlux.get(i/2).size(); j++){ //for each float in a band
				float val = 0.00000f;
					if ((spectralFlux.get(i/2).get(j) - thresholds.get(i/2).get(j)) > 0.00001f){
						val = (spectralFlux.get(i/2).get(j) - thresholds.get(i/2).get(j));
					}
					String value = (Math.round((val) * 100000f) / 100000f + ",");
					outputFileHelper(channel, value);
					k++;
			}

			stream.setLength(stream.length() - 1);
			//outputFileHelper(channel, ";" + k + "\n");
			outputFileHelper(channel, ";");
		}



		//end file output
		channel.close();
		stream.close();
		}catch (IOException e){
			e.printStackTrace();;
		}
	}

	static public void outputFileHelper(FileChannel channel, String value){
		try{
			byte[] strBytes = value.getBytes();
			ByteBuffer buffer = ByteBuffer.allocate(strBytes.length);
			buffer.put(strBytes);
			buffer.flip();
			channel.position(channel.size());
			channel.write(buffer);
		}catch (IOException e){
			e.printStackTrace();;
		}
	}
}
