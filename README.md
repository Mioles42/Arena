# Arena
## A genetic programming experiment. Currently paused.

Arena (AKA Ergo) was an ongoing project to create an accessible genetic programming system in the style of CRobots and old program-your-robot games.

### What it does
These robots move around, fire, eat pellets, and so forth autonomously. Future robots are randomly mutated from existing ones, with the idea that effective and/or novel robots will have more offspring and dominate, whereas weak or computationally expensive robots are gradually wiped out. All this can happen at high speed with no user input (though sometimes it's fun to slow things down and watch what's going on.)

The program contains a handful of other supporting features that are basically always under expansion when I have the time. It takes a while for anything interesting to happen with the robots; at the current stage of development, they can reliably evolve to circle around and erratically pick up pellets in about an hour at the highest speed. The genes that the robots can use has changed quite a bit during development, and will probably continue to.

### What's the point
I'm not an expert on genetic algorithms or machine learning... I'm a ~~high school~~ university student who watched some videos and thought these ideas were really cool. Because of that, Ergo's system for handling genetic algorithms is somewhat unusual. Robots have a list of "genes" from which they can build a couple different types of runnable memories. Those genes range from actions like "go forward" and "fire" to more subtle things like "store something in this memory location" or "calculate the bearing to another robot." The upside of this approach is that the result is something approximating assembly code - thick to read, but totally possible to analyze. There's actually a custom simple (and currently undocumented) language as part of Ergo that robots' runnable memories can compile to and from. 

There are also a few tidbits that make for some interesting possibilities with the right opening parameters. For instance, robots are capable of sharing information and discerning between different robots, meaning that communication across robots is possible (but still unlikely). Every time the program is run, the robots develop in a slightly different way, which is always interesting to watch. 

### Status
Development on Ergo has since stopped since I started university. Also, as a project it shows my reluctance to use external packages and libraries, instead relying on self-developed code. While that's great as a learning exercise, it means that managing components like graphics and physics becomes the bulk of the project. Right now, development is **paused** while I decide what I want to do with the project.

Enjoy!
