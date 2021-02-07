Introduction
============

This example program solves the problem of creating a 'self-counting' sentence,
where the sentence describes exactly how many occurrences of each of the
alphabet it contains. The sentence begins:

	"this sentence contains..."

and all numbers are written as words, i.e.'12' is written as "twelve".
If there is just one occurrence of a letter, it is written as "one 'j'".
If there is more than one occurrence an "s" is appended, e.g. "two 'g's".

This program relies on cross-over to quickly converge to a good solution,
but then relies on mutation and a large population to find an optimal solution.

There are three known solutions:

Counters:   3:1:3:2:35:6:2:7:11:1:1:2:1:22:15:1:1:5:26:21:1:7:8:3:5:1
this sentence contains three 'a's, one 'b', three 'c's, two 'd's, thirty five 'e's, six 'f's, two 'g's, seven 'h's, eleven  'i's, one 'j', one 'k', two 'l's, one 'm', twenty two 'n's, fifteen  'o's, one 'p', one 'q', five 'r's, twenty six 's's, twenty one 't's, one 'u', seven 'v's, eight 'w's, three 'x's, five 'y's and one 'z'

Counters:   3:1:3:2:33:7:1:6:10:1:1:1:1:24:16:1:1:9:26:17:5:5:4:4:4:1
this sentence contains three 'a's, one 'b', three 'c's, two 'd's, thirty three 'e's, seven 'f's, one 'g', six 'h's, ten  'i's, one 'j', one 'k', one 'l', one 'm', twenty four 'n's, sixteen  'o's, one 'p', one 'q', nine 'r's, twenty six 's's, seventeen  't's, five 'u's, five 'v's, four 'w's, four 'x's, four 'y's and one 'z'

Counters:   3:1:3:2:31:5:2:6:11:1:1:2:1:21:16:1:1:5:27:20:2:6:8:4:5:1
this sentence contains three 'a's, one 'b', three 'c's, two 'd's, thirty one 'e's, five 'f's, two 'g's, six 'h's, eleven  'i's, one 'j', one 'k', two 'l's, one 'm', twenty one 'n's, sixteen  'o's, one 'p', one 'q', five 'r's, twenty seven 's's, twenty  't's, two 'u's, six 'v's, eight 'w's, four 'x's, five 'y's and one 'z'


How it works
============
Despite there being 26 letters in the alphabet, a quick analysis shows that
the counts of 10 letters are constant (assuming that no count of any
individual letter goes above 100). Therefore an IntegerArrayChromosome
is used and seeded with random numbers in the range 1 to 40.
The genome has multiplicity 2 and a variety of ways can be used to determine
a counter value from an allele - see computeCounter in the anonymous phenotype
mapper class. Simply using one chromosome from the pair seems to converge
fairly well, allowing variation in the another chromosome that might help
during crossover for the next generation.

The fitness function just compares the counts for each letter from the genome
with how many are in the actual sentence. Each error value can be squared or
summed linearly. This appears to affect the probability of converging to the
different solutions.

This example program is a great way to experiment with the various components
from the library. One can experiment with the crossover and mutation
probabilities, crossover type, population container type, elitism, selector
and the genome structuring parameters multiplicity and number of parents. 

In its original form, it converges to a solution in roughly 30 to 50 generations.

