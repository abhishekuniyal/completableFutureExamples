*************************Completable Future******************************

CompletableFuture is used for asynchronous programming in Java. Asynchronous programming is a means of writing non-blocking code by running a task on a separate thread than the main application thread and notifying the main thread about its progress, completion or failure.

public class CompletableFuture<T> implements Future<T>, CompletionStage<T>{}

Future was introduced in Java 5:
Limitations of Future overcomed by CompletableFuture
* It cannot be manually completed.
* You cannot perform further action on a Future’s result without blocking.
* Multiple Futures cannot be chained together.
* You can not combine multiple Futures together.
* No Exception Handling.

** Important Methods associated to CompletableFuture (it has multiple other methods as well)
* To run Asynchronously
	supplyAsync ==> takes a supplier use if want to provide input
	runAsync    ==> takes a runnable use if dont want to provide input
* callback methods provided

	// thenApply() variants
	<U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
	<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
	<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)

	// thenAccept() variants
	public CompletableFuture<Void> thenAccept(Consumer<? super T> action)
	public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action)
    	public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor)


	// thenRun() variants
	public CompletableFuture<Void> thenRun(Runnable action) 
        public CompletableFuture<Void> thenRunAsync(Runnable action)
        public CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor)

* Methods for combining 2 CompletableFutures

	thenCompose() => eg. CompletableFuture<Double> cf3 = cf1.thenCompose(user-> cf2(user)); (thencompose(gives u faltten result)) vs thenApply)

	thenCombine() => eg. CompletableFuture<Double> cf3 = cf1.thenCombine(cf2, (c1result,c2result) -> {return c1result+c2result});


* Combining multiple Completable future
	CompletableFuture<Void> allOffResult = CompletableFuture.allOf(c1,c2,c3);
	CompletableFuture<Object> anyOffResult = CompletableFuture.anyOf(c1,c2,c3);

* Exceptions
	using callback exceptionally()
	using more generic method handle(req,res)
	whenComplete : Method whenComplete preserves the result of the triggering stage instead of computing a new one.
	(so whencomplete will print the detail exception while exp.getMessage whereas handle will not)
