# algae
Artifical life toolkit for Java

This project is a Java implementation of components for running experiments based on artificial-life/evolution-programming.
These techniques are useful in finding good (although not necessarily optimal) solutions to some otherwise tricky problems.

The process for using this library is as follows:

1. Decide how to encode possible solutions to a problem in a chromosome - several useful chromosome types are provided in the package `algae.chromosome`
1. Choose parameters and probability values for the genetic operators - several operator implementations are provided in the package `algae.operators`
1. Implement a phenotype mapper that uses values (alleles) from the chromosomes in the genome and constructs a potential solution to the problem
1. Implement a fitness tester that measures the ability of the candidate solution to solve the problem
1. Construct a `Parameters` instance with: a chromosome factory, phenotype mapper and a fitness tester
1. Set any other values in the `Parameters` instance if the default value is not desirable
1. Instantiate a `LifeCycle` with the `Parameters` instance
1. Call the method `initGeneration()` and then repeatedly call `runGeneration()`
1. If/when the return value from either of the above two methods is true then an optimal solution has been found.

For example programs, look in the package `functional_test`.

The Ant build file `build.xml` is used to compile the source code, run the unit tests and build a jar file.
Unfortunately, the unit test execution step is not working at the moment when invoked from GitHub's continuous integration,
but it can be invoked locally, assuming you have Ant installed.
