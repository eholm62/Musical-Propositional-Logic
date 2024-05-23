## What is this project about? 
In 2023, my AP Computer Science A teacher quickly introduced the class to
propositional logic in order to give us a better understanding of boolean 
expressions in Java. One major part of propositional logic we discussed 
was the construction of truth tables. If you're unfamiliar, truth tables
display every possible combination of truth values assigned to the atomic
statements in the expression, and what each of those combinations yields
as the value of the whole expression.

For example, if you have the atomic statments **P** and **Q**, and the 
proposition **P v Q** (read as "P or Q"), you can construct the truth table for that 
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
