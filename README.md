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
import com.mosesn.pirate.Pirate

val helpText = "[ -fad ]"
val arguments = " -a".split(" ")
Pirate(helpText)(arguments)
```

##Examples
###wc
```scala
import com.mosesn.pirate.Pirate

val helpText = "[ -clmw ] [file]"
val arguments = " -l /usr/share/dict/words".split(" ")
Pirate(helpText)(arguments)
```

###uniq
```scala
import com.mosesn.pirate.Pirate

val helpText = "[-cdu] [-i] [-f int] [-s int] [input_file [output_file]]"
val arguments = "-c -i -f 3 sorted_file".split(" ")
Pirate(helpText)(arguments)
```

###ls
```scala
import com.mosesn.pirate.Pirate

val helpText = "[-ABCFGHLOPRSTUWabcdefghiklmnopqrstuwx] [file]"
val arguments = " -a -l ".split(" ")
Pirate(helpText)(arguments)
```

## Install
So you want to install pirate, eh?  Put this in your build.sbt file:  
```scala
resolvers += "sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq("com.mosesn" %% "pirate" % "0.1.0")
```

##Help Text
Help text comes in several different flavors.  There are flags, values, and strings.  

###Flags
A flag looks like this: "-f".  Flags can be put together, like "-fad".  These flags  
mean that you expect a boolean value, whether the argument "-f" or any one or more  
of the arguments in "-fad" are passed to you.

###Values
A value looks like this: "-v type" where type is the datatype you expect to have passed in.  
An example might be, "-n int" or "-D double".  The different types that are supported are  
int, double, and string.

###Strings
There are also named strings, which is especially useful for filenames.  A string can be  
named anything except for the reserved keywords int, string, and double, and may look like  
"input".

###Options
You may not want to pass in flags all of the time, so there is the option to not pass in flags.  
This is denoted with square brackets.  These can wrap anything, and always mean that passing them  
in is optional.

##Arguments
Should be an array of Strings of flags, no square brackets, started by a hyphen.  
Example: " -faddd " (whitespace is fine, except in the flags).

###Flags
At least one flag must be passed in for a group of flags to be considered covered.  Multiple  
flags can be passed in either by repeating them, or by passing multiple flags back together.  
An example of that would be for " -acdf ", you can turn on both a and d by passing back either  
* "-a -d"
* "-ad"
which are both considered valid.

###Optional Arguments
However, if you have one thing inside of the square brackets, you are  
expected to match everything.  For example, if you have "[ -ab -cd ]", valid strings to  
pass in would be "-a -c",  "-ab -d", "-dc -ba", "-d -d -b -a" and "".  However, "-ab"  
would NOT be valid, because you don't include any of the flags from "-cd".

###Order
Flags can be passed in any order, and multiple times.  All times will be considered valid.  Values  
can only be passed in once, but they are  also in any order.  However, they can only be passed  
in any order within their "context".  For example, in "[ -a [ -c -d ]]", valid arguments are all  
strings where:
* The empty string is passed in.
* Only the -a argument is passed in.
* Both the -a and the -c and -d arguments are passed in, where -c and -d are always next to each other.  
An example of the simplest invalid string with all of the right flags, just in the wrong order, is  
"-c -a -d".

##TODO
I have been following the suggestions on how to handle arguments from [The Art of Unix Programming](http://www.faqs.org/docs/artu/ch10s05.html).  
  
The UNIX style of parsing arguments is basically completely implemented, with only small adjustments  
that might be desirable, such as adding the option of specifying which values are mutually exclusive,  
although since there isn't a preexisting way of showing that in help text, it isn't a high priority.  
  
However, GNU style arguments haven't been implemented at all.  If there is interest, I don't think they  
will be very hard to do, but I personally prefer UNIX style, so if no one else is using this, I won't  
bother.  If you would like the functionality, feel free to file a github issue on me or email me about it.

##Contributors
[Moses Nakamura](http://github.com/mosesn)