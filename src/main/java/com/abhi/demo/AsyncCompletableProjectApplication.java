package com.abhi.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AsyncCompletableProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncCompletableProjectApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner() {
		return args -> {

			Supplier<String> firstNameSupplier = () -> {
				return "Jeff";
			};
			Supplier<String> lastNameSupplier = () -> {
				return "Bezos";
			};

			/************ Callbacks ************************************************/
			CompletableFuture<Void> firstNameFuture = CompletableFuture.supplyAsync(firstNameSupplier) // to start
																										// supplyAsyn or
																										// runAsyn
					/*
					 * thenApply --> takes Function and apply the result variations:
					 * thenApply(Function, Executor) ; if you want to to provide own Executor(fixed
					 * or cache etc) instead of default Forkjoin thenApplyAsync ; will execute apply
					 * code in a separate thread asynchronously
					 */
					.thenApply(name -> {
						return "Mr/Mrs " + name;
					})
					/*
					 * thenAccept -> takes consumer variations: thenAccept(Function, Executor) ; if
					 * you want to to provide own Executor(fixed or cache etc) instead of default
					 * Forkjoin thenAcceptAsync ; will execute apply code in a separate thread
					 * asynchronously
					 */
					.thenAccept(name -> System.out.println("name with salutation is: " + name));

			// firstNameFuture.get();

			CompletableFuture<String> lastNameFuture = CompletableFuture.supplyAsync(lastNameSupplier)
					.thenApply(name -> {
						return name;
					});

			firstNameFuture.thenRun(() -> System.out.println("First name calculated..."));

			/************ Callbacks Ends ************************************************/

			/************
			 * Composing 2 CompletableFutures
			 ************************************************/
			System.out.println("Compose started****");
			thenComposeExample();
			System.out.println("Compose Ended****");
			/************
			 * Composing 2 CompletableFutures Ends
			 ************************************************/

			/************
			 * Combining 2 CompletableFutures
			 ************************************************/

			thenCombineExample();
			/************
			 * Combining 2 CompletableFutures Ends
			 ************************************************/

			/************
			 * Combining multiple CompletableFutures
			 ************************************************/
			CompletableFuture<Void> allOf = CompletableFuture.allOf(firstNameFuture,lastNameFuture);
			CompletableFuture<Object> anyOf = CompletableFuture.anyOf(firstNameFuture,lastNameFuture);
			/************
			 * Combining multiple CompletableFutures
			 ************************************************/
			
			exceptionally();
			handle();
			whenComplete();
			/*****************Some other code snippet you could use*Starts****************/
			CompletableFuture<?> complete = CompletableFuture.completedFuture("any value");
			System.out.println(complete.isDone()); //true
			complete.whenComplete((x,y)-> System.out.println("x::"+x+"::y::"+y));
			/*****************Some other code snippet you could use* Ends****************/
		};
	}

	private void whenComplete() {
		String str = null;
		CompletableFuture<String> value = CompletableFuture.supplyAsync(() -> {
		  if (str == null)
		    throw new IllegalArgumentException("Invalid String value passed " + str);
		  return str;
		}).whenComplete((s, exp) -> {
		  System.out.println("in whenComplete method");
		  if(exp != null) {
		    System.out.println("Exception thrown with message - " + exp.getMessage());
		    //s = "";
		  }
		});

		
	}

	private void handle() {
		String str = null;
		CompletableFuture<String> value = CompletableFuture.supplyAsync(() -> {
		  if (str == null)
		    throw new IllegalArgumentException("Invalid String value passed " + str);
		  return str;
		}).handle((s, exp) -> {
		  if(exp != null) {
		    System.out.println("Exception thrown with message - " + exp.getMessage());
		    s = "";
		  }
		  return s;
		});
		
	}

	private void exceptionally() {
		String str = null;
		CompletableFuture<String> value = CompletableFuture.supplyAsync(() -> {
		  if (str == null)
		    throw new IllegalArgumentException("Invalid String value passed " + str);
		  return str;
		}).exceptionally(exp -> {
		  System.out.println("Exception thrown with message - " + exp.getMessage());
		  return "";
		});
		
	}

	private void thenComposeExample() throws InterruptedException, ExecutionException {

		List<Users> users = Arrays.asList(new Users(1, "User1", "User1Dep", 1000),
				new Users(2, "User2", "User2Dep", 2000), new Users(3, "User3", "User3Dep", 500));

		CompletableFuture<List<Users>> eligibleUsers = CompletableFuture.supplyAsync(() -> {
			List<Users> eligibleUser = new ArrayList<>();

			return users.stream().filter(u -> u.getSal() > 500).collect(Collectors.toList());
		});
		
		CompletableFuture<List<String>> eligibleUsersName = eligibleUsers.thenCompose(eligibleUsersList -> eligibleUserNames(eligibleUsersList));
		
		//if(eligibleUsersName.isDone()) {
			eligibleUsersName.get().forEach(System.out::println);
		//}
			
	}

	CompletableFuture<List<String>> eligibleUserNames(List<Users> users) {
		return CompletableFuture.supplyAsync(() -> {
			return users.stream().map(Users::getUserName).collect(Collectors.toList());
		});

	}

	private void thenCombineExample() throws InterruptedException, ExecutionException {

		CompletableFuture<Integer> business1Revenue = CompletableFuture.supplyAsync(() -> {
			System.out.println("Calculating revenue for business1");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 100000000;
		});
		CompletableFuture<Integer> business2Revenue = CompletableFuture.supplyAsync(() -> {
			System.out.println("Calculating revenue for business2");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 200000000;
		});

		CompletableFuture<Integer> totalRevenue = business1Revenue.thenCombine(business2Revenue, (rev1, rev2) -> {
			return rev1 + rev2;
		});

		System.out.println("Total Revenue is ::" + totalRevenue.get());
		System.out.println("Total Revenue 1 is ::" + totalRevenue.join());

	}

}
