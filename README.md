Home Myo
========

Home Myo is Android application which was developed as proof-of-concept of advanced Smart Homes control using hand gestures and 
indoor localization. It was developed as part of mine Bachelor thesis on topic *Intelligent Building Control by 
Hand Gestures*, which you can find [here](https://dspace.cvut.cz/handle/10467/61122).

This application connects two components of the system:
* [Myo armband](https://www.myo.com/) for hand gesture recognition
* Basic in-house developed localization system

Short demonstration of the system can be found bellow:
[![Home Myo Demonstration](http://img.youtube.com/vi/8Ie8_JYjdCQ/0.jpg)](http://www.youtube.com/watch?v=8Ie8_JYjdCQ)

Currently application can be run only in specific test Smart Home at Czech Technical University, since for it use
special local protocol for comunication with the Smart Home. The sames goes for the localization system.
But application was designed with this in mind and it provide easy way how to implement different Adapters for communication
and localization.

More in-depth information about architecture of the system can be found in earlier mentioned Bachelor thesis.