Fractaloscope
=============

**Fractaloscope** is a simple fractal image viewer written in Java. It is created as a university coursework.

Several types of fractal images can be generated:
* [Koch](http://en.wikipedia.org/wiki/Koch_curve) & [Cesaro](http://mathworld.wolfram.com/CesaroFractal.html) curves with arbitrary angles
* [Dragon curve](http://en.wikipedia.org/wiki/Dragon_curve)
* [Barnsley fern](http://en.wikipedia.org/wiki/Barnsley_fern)
* [Julia set](http://en.wikipedia.org/wiki/Julia_set)

A bit of [Prefuse library](http://prefuse.org/) is used for color scale generation.

The main feature of this viewer is its ultra-fast realtime rendering. It uses the Fork/Join framework, which optimally utilizes multi-core CPUs for fractal computation. As a result, rendering times are dramatically improved. For example, on a Windows 7 machine with Intel Core i7 CPU it was capable of rendering a 1000x1000 image of a Julia set with constantly changing seed at 30+ fps.

Such rendering speed allows the user to interact with a fractal seamlessly, changing its parameters and observing the image change in real-time without waiting for it to render. Dragging and zooming (which is only limited by precision of a double) are also supported.