# Beatakt - Rhythm game using FFT to generate beat maps in LibKtx

Working build, as seen in my Thesis on it. May or may not continue working on it later. Currently there is a limitation to the algorithm, LibGtx uses its own javazoom, which is stripped off the components I need for it. A solution would be to include the FFT part on its own, then have the game call the .jar file with some args about the song position. It might not be worth it, and a separate algorithm might have to be used to make a "whole" game, but as it is its a good proof of concept that you can make such a game.

To run, first run the fftCalculation in fft package, on its own to generate the map, and then point to it inside of the game. Don't forget to add the length. If it wasn't for javazoom, you would have gotten the location through scanning music with gdx.files.external recursive calls, which would forward the song location to the algorithm, and then the algorithm, using the MP3 decoder, would calculate the length of the song for you.

[Video Demo of Game](video_demo.mp4)