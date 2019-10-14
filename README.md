# Hadoop_examples
Examples from Bootcamp based on hadoop ecosystem <br>
###Using MapReduce to implement Google Auto-Completion based on N-Gram Language Model. <br>

Step 1: Build N-Gram library over Wiki dataset, then build Language Model based on statistical probability distribution, generate structured data and store it in database. <br>
step 2: Using PHP/Ajax and jQuery to retrieve data from database to implement real-time auto-completion. Then display the auto-complete features of search engine on web page just like Google does. <br>

In the fields of computational linguistics and probability, an n-gram is a contiguous sequence of n items from a given sample of text or speech. An n-gram model is a type of probabilistic language model for predicting the next item in such a sequence in the form of a (n − 1)–order Markov model.

An n-gram model models sequences, notably natural languages, using the statistical properties of n-grams.

This idea can be traced to an experiment by Claude Shannon's work in information theory. Shannon posed the question: given a sequence of letters (for example, the sequence "for ex"), what is the likelihood of the next letter? From training data, one can derive a probability distribution for the next letter given a history of size {\displaystyle n}n: a = 0.4, b = 0.00001, c = 0, ....; where the probabilities of all possible "next-letters" sum to 1.0.

More concisely, an n-gram model predicts {\displaystyle x_{i}}x_{i} based on {\displaystyle x_{i-(n-1)},\dots ,x_{i-1}}x_{i-(n-1)}, \dots, x_{i-1}. In probability terms, this is {\displaystyle P(x_{i}\mid x_{i-(n-1)},\dots ,x_{i-1})}P(x_{i}\mid x_{{i-(n-1)}},\dots ,x_{{i-1}}). When used for language modeling, independence assumptions are made so that each word depends only on the last n − 1 words. This Markov model is used as an approximation of the true underlying language. This assumption is important because it massively simplifies the problem of estimating the language model from data. In addition, because of the open nature of language, it is common to group words unknown to the language model together.

Note that in a simple n-gram language model, the probability of a word, conditioned on some number of previous words (one word in a bigram model, two words in a trigram model, etc.) can be described as following a categorical distribution (often imprecisely called a "multinomial distribution").

In practice, the probability distributions are smoothed by assigning non-zero probabilities to unseen words or n-grams; see smoothing techniques.
