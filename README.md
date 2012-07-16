#pirate
pirate parses arrrrguments.

##Idea
The idea behind pirate is that option parsing for command line tools should  
be easier.  The best mechanism that I've seen so far for command line tool  
option parsing has been a builder python pattern (I forget what it's called)  
and it seemed like a pain in the ass even with it as simple as it was.  What  
I wanted was to be able to just give a program the help text and for it to  
figure out how to parse the arguments.  The format should be:
  
```scala
val helpText = "[ -fad ]"
val arguments = " -a"
Argh(helpText)(arguments)
```
  
Right now, it only supports single character flags, and for the arguments to  
be in a single string, which sort of sucks.  However, I'm working on it in a  
fun TDD way that I haven't experimented that much with before, and because the  
way it should function is so straight forward, TDD works well for it.

##History
pirate used to be Argh, but I realized I was doing it completely wrong and  
that I didn't understand how scala parsers worked, so I started over again  
from the beginning.  I still don't know whether I prefer pirate or Argh,  
and I might want to return to my Argh ways once I've figured out pirate, but  
I've been able to be much more expressive by just parsing based on the raw  
parsing library, rather than the RegexParsers String parsing library, which  
did too much for me.

##DSL

###Help Text
Should all be a single String of flags in square brackets, started by a hyphen.  
Example: "  [ -fad ] " (whitespace is fine, except in the flags).

###Arguments
Should all be a single String of flags, no square brackets, started by a hyphen.  
Example: " -faddd " (whitespace is fine, except in the flags).