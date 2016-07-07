Contributing to justext-java
=============================

Justext-java is an open source project and we love to receive contributions from our community! There are many ways to contribute, from writing tutorials or blog posts, improving the documentation, submitting bug reports and feature requests or writing code which can be incorporated into justext-java itself.

Bug reports
-----------

If you think you have found a bug, first make sure that you are testing against the latest snapshot version, your issue may already have been fixed. If not, search our [issues list](https://github.com/wizenoze/justext-java/issues) on GitHub in case a similar issue has already been opened.

It is very helpful if you can prepare a reproduction of the bug. In other words, provide a small test case which we can run to confirm your bug. It makes it easier to find the problem and to fix it. Test cases can be provided as a Groovy script or as a link to a small java projet. Provide as much information as you can.

Feature requests
----------------

If you find yourself wishing for a feature that doesn't exist, you are probably not alone. Open an issue on our [issues list](https://github.com/wizenoze/justext-java/issues) on GitHub which describes the feature you would like to see, why you need it, and how it should work.

Contributing code and documentation changes
-------------------------------------------

If you have a bugfix or new feature that you would like to contribute to justext-java, please find or open an issue about it first. Talk about what you would like to do. It may be that somebody is already working on it, or that there are particular issues that you should know about before implementing the change.

We enjoy working with contributors to get their code accepted. There are many approaches to fixing a problem and it is important to find the best approach before writing too much code.

### Fork and clone the repository

You will need to fork the main justext-java code or documentation repository and clone it to your local machine. See
[github help page](https://help.github.com/articles/fork-a-repo) for help.

Further instructions for specific projects are given below.

### Submitting your changes

Once your changes and tests are ready to submit for review:

1. Test your changes

    Run the test suite to make sure that nothing is broken. `mvn clean test`

2. Rebase your changes

    Update your local repository with the most recent code from the main justext-java repository, and rebase your branch on top of the latest master branch. We prefer your initial changes to be squashed into a single commit. Later, if we ask you to make changes, add them as separate commits.  This makes them easier to review.  As a final step before merging we will either ask you to squash all commits yourself or we'll do it for you.

3. Submit a pull request

    Push your local changes to your forked copy of the repository and [submit a pull request](https://help.github.com/articles/using-pull-requests). In the pull request, choose a title which sums up the changes that you have made, and in the body provide more details about what your changes do. Also mention the number of the issue where discussion has taken place, eg "Closes #123".

Then sit back and wait. There will probably be discussion about the pull request and, if any changes are needed, we would love to work with you to get your pull request merged into justext-java.

Please adhere to the general guideline that you should never force push
to a publicly shared branch. Once you have opened your pull request, you
should consider your branch publicly shared. Just add incremental commits; this is generally easier on your
reviewers. If you need to pick up changes from master, you can merge
master into your branch. A reviewer might ask you to rebase a
long-running pull request. Note that squashing at the end of the review process should
also not be done, that can be done when the pull request is [integrated
via GitHub](https://github.com/blog/2141-squash-your-commits).

Contributing to the justext-java codebase
------------------------------------------

**Repository:** [https://github.com/wizenoze/justext-java/](https://github.com/wizenoze/justext-java/)

Make sure you have [Maven](https://maven.apache.org/) installed, as
justext-java uses it as its build system.

Please follow these formatting guidelines:

* Java indent is 4 spaces
* Line width is 120 characters
* There's a new line at the end of file
* Add a package-info.java for each new package
* Add copyright header (LICENSE.txt) to each new file
* Public classes, methods and variables should have a javadoc
* Wildcard imports (`import foo.bar.baz.*`) are forbidden
* Disable “auto-format on save” to prevent unnecessary format changes. This makes reviews much harder as it generates unnecessary formatting changes and might cause the build to fail. Please attempt to tame your IDE so it doesn't make them.

To create a distribution from the source, simply run:

```sh
mvn clean install
```

Before submitting your changes, run the test suite to make sure that nothing is broken, with:

```sh
mvn test
```
