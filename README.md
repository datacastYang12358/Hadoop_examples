# Hadoop_examples
Examples from Bootcamp based on hadoop ecosystem <br>
### Using MapReduce to implement Auto-Completion based on N-Gram Language Model. <br>

Step 1: Build N-Gram library over dataset, then build Language Model based on statistical probability distribution, generate structured data and store it in database. <br>
step 2: Retrieve data from database to implement real-time auto-completion. Then display the auto-complete features of search engine on web page just like Google does. <br>

An n-gram model models sequences of n items from a given sample of text or speech is a type of probabilistic language model for predicting the next item in such a sequence in the form of a (n − 1)–order Markov model.<br>

The idea is: given a sequence of letters, what is the likelihood of the next letter? From training data, one can derive a probability distribution for the next letter given a history of size, where the probabilities of all possible "next-letters" sum to 1.0.<br>

More concisely, when used for language modeling, independence assumptions are made so that each word depends only on the last n − 1 words. This Markov model is used as an approximation of the true underlying language. This assumption is important because it massively simplifies the problem of estimating the language model from data. In addition, because of the open nature of language, it is common to group words unknown to the language model together.<br>

Note that in a simple n-gram language model, the probability of a word, conditioned on some number of previous words (one word in a bigram model, two words in a trigram model, etc.) can be described as following a categorical distribution (often imprecisely called a "multinomial distribution").<br>
