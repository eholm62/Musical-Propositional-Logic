## What is this project about? 
In 2023, my AP Computer Science A teacher quickly introduced the class to
propositional logic in order to give us a better understanding of boolean 
expressions in Java. One major part of propositional logic we discussed 
was the construction of truth tables. If you're unfamiliar, truth tables
display every possible combination of truth values assigned to the atomic
statements in the expression, and what each of those combinations yields
as the value of the whole expression.

For example, if you have the atomic statments `P` and `Q`, and the 
proposition `P v Q` (read as "P or Q"), you can construct the truth table for that 
proposition like so. First, order the atomic statements alphabetically.

![image](https://github.com/eholm62/Musical-Propositional-Logic/assets/133877996/9e00b344-cd2d-45dc-ad51-160d70639ba9)

To the right of that, write out the full proposition. Now, count in binary, where T is 1 and F is 0, 
starting at 0, going to 3 (0b11). The ones place should correspond to the last atomic statement 
alphabetically, the twos place to the second to last, fours place third to last, and so on. For
our example, you should end up with something like this.

![image](https://github.com/eholm62/Musical-Propositional-Logic/assets/133877996/24fd792a-70d0-4d38-a5b2-d943440f38b6)

Finally, evaluate the expression for each of those combinations of truth values, and write the
result to the right. Having done that, you'll have a fully completed truth table.

![image](https://github.com/eholm62/Musical-Propositional-Logic/assets/133877996/6ae7a584-fb16-4ef0-a04d-a3e41e119787)

I noticed that sometimes reading out the last column of the truth table creates in an interesting rhythm.
Each T represents a hit, and each F represents no hit. Each new row is a new beat. I decideed to write
a program that takes in a propositional and plays you back the resulting rhythm. I thought that being 
able to conceptualize a proposition as a rhythm could possibly unlock new intuition into the way 
propositions work, and if not, it still has the potential to create interesting rythyms. 

I expanded the scope of the project such that there are four possible scenarios on each beat. Those are:
rest, ghost, click, and accent. The new possibilities are ghost and accent, because rest and click are
the same two scenarios we already had. Ghost is like a much softer version of click, and accent is 
like a click with emphasis on it. 

The way the program determines the scenario for each beat is by making use of one primary and one
secondary proposition. The two propositions must have the same number of possible combinations. 
If both the primary and secondary propositions are false, there is a rest on that beat. If the
secondary is true but the primary is false, it's a ghost. If the primary is true and the secondary
is false, it's just a regular click. If both the primary and secondary are true, it's an accent.

## How to use it
To run the program, navigate to the base directory of the project if you aren't yet there. Then, run
the following command `. bash_config.sh`. This only has to be done once unless a new terminal has been opened
or if your aliases or classpath have been modified. Once you've worked that out, run main with this template: 
`java Main "[primary]" "[secondary]" [beat length]`. Insert your primary and secondary propositions within quotes,
and the leangth of one beat in milliseconds. You may also add one extra parameter at the end, `false`, if you would
like the program to run silently and only print a visual representation of the rhythm. Here is an example of a
valid execution: `java Main "-A v B ^ C > D <> E" "A V B V C V D V E" 100`.

If you want to only take the first proposition into account and essentially ignore the second, there is a way to do it,
but it's kind of annoying to do. If at some point I come back to this project, I will make it easier to do this.
Until then, you have to construct a secondary proposition that is always false, and has the same number of possible 
combinations. You can do this by reusing the same atomic statements from the primary proposition in the secondary proposition. 
Connect them all with the conjunction (AND). Then, switch any one of the atomic statements `A`, with the expression `(A ^ -A)`.
For example, if you have the primary expression `A V B ^ -C > D <> E`, you can provide the secondary expression 
`A ^ B ^ C ^ D ^ (E ^ -E)`, in order to make sure only the primary proposition contributes to the rhythm.

If you want to use your own sound effects you can delete the previously existing wave files and replace 
them with your own, making sure they're named the same as the original files. The shorter the sound effects,
the shorter you can make the beat length. In general the beat length should not be any shorter than the length
of the longest sound effect.

Finally, If you're trying to create interesting rhythms, just remember that the more complex the propositions,
the more complex the rhythms. More complex rhythms allow for the possibility of more interesting rhythms, but
also for the possibility of unlistenable rhythms. What I suggest is to experiment by connecting multiple propositions
into one using logical connectives. One basic thing this can do is combine two ryhtyms into one. For a basic example,
assume you have the proposition `A <> B`, which gives you the rhythm `[true, false, false, true]`, and the proposition
`-A ^ B`, which gives you the rhythm `[false, true, false, false]`. You can add these two together like this:
`(A <> B) v (-A ^ B)`, which gives you the rhythm `[true, true, false, true]`, the combination of the two 
rhythms produced by the sub-propositions.
