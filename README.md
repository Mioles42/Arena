# Arena
A genetic programming experiment.

Arena (AKA Ergo) is an ongoing project to create an accessible genetic programming system in the style of CRobots and old program-your-robot games.

These robots move around, fire, eat pellets, and so forth autonomously. Future robots are randomly mutated from existing ones, with the idea that effective and/or novel robots will have more offspring and dominate, whereas weak or computationally expensive robots are gradually wiped out. All this can happen at high speed with no user input (though sometimes it's fun to slow things down and watch what's going on.)

I'm not an expert on genetic algorithms or machine learning... I'm a high school student who watched some videos and thought these ideas were really cool. To that end Ergo's system for handling genetic algorithms is somewhat unusual. Robots have a list of "genes" from which they can build a couple different types of runnable memories. Those genes range from actions like "go forward" and "fire" to more subtle things like "store something in this memory location" or "calculate the bearing to another Robot." The upside of this approach is that the result is something approximating assembly code - thick to read, but totally possible to analyze.

There are also a few tidbits that make for some interesting possibilities with the right opening parameters. For instance, robots are capable of sharing information and discerning between different Robots, meaning that communication across Robots is possible (but still unlikely). Every time the program is run, the Robots develop in a slightly different way, which is always interesting to watch.

Enjoy!
