# Beatakt - Rhythm game using FFT to generate beat maps. Made with LibKtx

A rhythm game similar to Guitar Hero where levels can be generated dynamically using a FFT algorithm for any song. Currently FFT part of the game is separate, with efforts being made to make this work on Android. Currently, FFT component cannot run on Android as Android lacks javax.sound in its implementation of Java. Effort is being made to have a workaround on a separate branch. 

Main branch has a working build of the game where music and the generated level for it is build into the assets folder as a test/proof of concept. If FFT can be made to run on Android, music would be sourced from device storage instead.

This was initially made for my final Uni project, and I am now reviving development in the hopes to complete this in the near future.
[Video Demo of Game](video_demo.mp4)