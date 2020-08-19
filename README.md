# 🛠️ LABORATORIO 1 
  📌 **Angi Paola Jiménez Pira**
## 📄 Black List Search
### Part I - Introduction to threads in JAVA
  
  1. In agreement with the lectures, complete the classes CountThread, so that they define the life cycle of a thread that prints the numbers between A and B on the screen.
 
 2. Complete the main method of the CountMainThreads class so that:
      
      1. Create 3 threads of type CountThread, assigning the first interval [0..99], the second [99..199], and the third [200..299]. 
      
      2. Start the three threads with start(). Run and check the output on the screen. 
      
      3. Change the beginning with start() to run(). How does the output change? Why?
      
         * Salida con el método `start()`:
           
            ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/salida_start.png) 
          
         * Salida con el método `run()`:
           
            ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/salida_run.png) 
      
         Se observa que la salida cambia, esto se debe a que cuando se ejecuta `thread1.run()`, lo que sucede es que se llama directamente al método `run()` de thread1 el
         cual es un método secuencial normal, por lo tanto, hasta que no termine de ejecutarse `thread1.run()` no se ejecutará `thread2.run()`; por otro lado, si se ejecuta
         `thread1.start()` se crea el hilo y si luego se llama a `thread2.start()` se crea otro hilo y estos se ejecutan paralelamente.
   
### Part II - Black List Search Exercise

  For an automatic computer security surveillance software, a component is being developed to validate the IP addresses in several thousands of known malicious blacklists (of
  malicious hosts), and to report those that exist in at least five of said lists.
  
  Said component is designed according to the following diagram, where:
 
 * HostBlackListsDataSourceFacade is a class that offers a facade for queries in any of the N registered blacklists (method 'isInBlacklistServer'), and which also allows a
 report to a local database of when an IP address is considered dangerous. This class is NOT MODIFIED, but it is known to be 'Thread-Safe'.
  
 * HostBlackListsValidator is a class that offers the checkHost method, which, through the HostBlackListDataSourceFacade class, validates a given host in each of the
  blacklists. In this method is considered the policy that when a HOST is found in at least five blacklists, it will be registered as not reliable, or as reliable otherwise.
  Additionally, it will return the list of the numbers of the blacklists where the HOST was registered.
    
   ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/Model.jpg)
 
 When using the module, the evidence that the registration was made as reliable or not reliable is given by the messages of LOGs:
 
    INFO: HOST 205.24.34.55 Reported as trustworthy
    INFO: HOST 205.24.34.55 Reported as NOT trustworthy
    
The test program provided (Main), takes only a few seconds to analyze and report the address provided (200.24.34.55), since it is registered more than five times in the first servers, so it does not need to cover all of them. However, doing the search in cases where there are NO reports, or where they are scattered in the thousands of blacklists, takes a lot of time.

This, like any search method, can be seen as a shamefully parallel problem, since there are no dependencies between one part of the problem and another.

To refactor this code, and have it exploit the multi-core capability of the computer's CPU, do the following:

  1. Create a Thread class that represents the life cycle of a thread that searches for a segment of the pool of available servers. Add to that class a method that allows you
  to ask the instances of it (the threads) how many occurrences of malicious servers it has found or found.
  
  2. Add to the checkHost method an integer parameter N, corresponding to the number of threads between which the search will be carried out (remember to take into account if
  N is even or odd!). Modify the code of this method so that it divides the search space between the indicated N parts, and parallels the search through N threads. Have that
  function wait until the N threads finish solving their respective sub-problem, add the occurrences found for each thread to the list that returns the method, and then
  calculate (adding the total number of occurrences found for each thread) if the Number of occurrences is greater than or equal to BLACK_LIST_ALARM_COUNT. If this is the
  case, in the end the host MUST be reported as reliable or not reliable, and the list should be shown with the numbers of the respective blacklists. To achieve this wait
  behavior, review the join method of the Java concurrency API. Also keep in mind:
  
     1. Inside the checkHost method the LOG must be kept informing, before returning the result, the number of revised blacklists VS. the number of total blacklists (line
     60). It must be guaranteed that said information is true under the new parallel processing scheme proposed.
   
     2. It is known that HOST 202.24.34.55 is blacklisted in a more dispersed way, and that host 212.24.24.55 is NOT on any blacklist.
     
 ### Part III - Discussion
 
 The strategy of parallelism previously implemented is inefficient in certain cases, since the search is still carried out even when the N threads (as a whole) have already
 found the minimum number of occurrences required to report to the server as malicious. How could the implementation be modified to minimize the number of queries in these
 cases? What new element would this bring to the problem?
 
  Para minimizar el número de consultas en estos casos, se podría poner como estática la variable que lleva el conteo de ocurrencias en la clase que representa el ciclo de
  vida de un hilo que busca en un segmento del grupo de servidores disponibles. De esta manera todos los hilos tendrán el mismo valor en esta variable y así todos sabrán
  cuando se llegue al número mínimo de ocurrencias requeridas para reportar al servidor como malicioso para no seguir realizando más consultas. Con esto se incluye un 
  problema de seguridad que se puede solucionar haciendo la misma variable atómica para que no se presenten condiciones de carrera.
  
 ### Part IV - Performance Evaluation 
  From the above, implement the following sequence of experiments to perform the validation of dispersed IP addresses (for example 202.24.34.55), taking the execution times
  of them (be sure to do them on the same machine):

  1. A single thread. 
  2. As many threads as processing cores (have the program determine this using the Runtime API). 
  3. As many threads as twice the number of processing cores. 
  4. 50 threads 
  5. 100 threads
  
  When starting the program run the monitor jVisualVM, and as you run the tests, review and record the CPU and memory consumption in each case.
  
   1. A single thread
      
      ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/visual-1hilo.png)
    
   2. As many threads as processing cores(4 threads):
     
      ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/visual-4hilos.png)
    
   3. As many threads as twice the number of processing cores(8 threads):
     
      ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/visual-8hilos.png)

   4. 50 threads
     
      ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/visual50hilos.png)
    
   5. 100 threads
    
      ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/visual-100hilos.png)
      
   Para todos los casos se obtiene la siguiente salida, ya que se uso la misma dirección ip:
      
   ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/salida-1hilo.png)
    
  With the above, and with the given execution times, make a graph of solution time vs. Number of threads. Analyze and hypothesize with your partner for the following
  questions (you can take into account what was reported by jVisualVM):
  
   ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/ghilos.png)
  
   * According to Amdahls law, where S(n) is the theoretical improvement of performance, P the parallel fraction of the algorithm, and n the number of threads, the greater
    n, the better this improvement should be. Why is the best performance not achieved with the 500 threads? How is this performance compared when using 200 ?.
   
     ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/BLACKLISTSEARCH/img/ahmdahls.png)
     
     La ley de Amdahl establece que la mejora obtenida en el rendimiento de un sistema debido a la alteración de uno de sus componentes está limitada por la fracción de
     tiempo que utiliza dicha mejora en la ejecución de la tarea. Al implementar hilos en nuestro programa de búsqueda, estamos introduciendo una mejora y pudimos comprobarlo
     al incrementar el numero de hilos, por ejemplo de 1 a 4, observamos que el tiempo de ejecución disminuyó de 1 minuto 43 segundos a 35 segundos, sin embargo, al usar 50 y
     100 hilos la diferencia entre los tiempos de ejeución fue tan solo de 0,04 segundos; esto también es explicado por la ley de Amdahl, que establece que la mejora
     incremental en la aceleración conseguida con la mejora de una parte se va reduciendo a medida que se van introduciendo nuevas mejoras, es decir, al medida que aumentamos
     el número de hilos el tiempo de ejecución que nos ahorraremos será menor, esto explicaría también porque no se lograría un mejor rendimiento con 500 hilos, dicho 
     rendimiento seria muy parecido al que obtendríamos usando 200 hilos.
     
   * How does the solution behave using as many processing threads as cores compared to the result of using twice as much?
   
     Usando tantos hilos de procesamiento como núcleos, es decir, 4 hilos el programa tarda 35 segundos en hacer la búsqueda, si se usa el doble de hilos, el programa tarda
     18 segundos, evidenciamos que el tiempo de ejecución se reduce casi a la mitad.
        
   * According to the above, if for this problem instead of 100 threads in a single CPU could be used 1 thread in each of 100 hypothetical machines, Amdahls law would apply
    better ?. If x threads are used instead of 100/x distributed machines (where x is the number of cores of these machines), would it be improved? Explain your answer.
    
     Si se corren 100 hilos, cada uno en una CPU distinta, podríamos pensar que el objetivo se alcanzaría en un tiempo menor, pero esto no es del todo cierto ya que deben
	 agruparse los resultados de todos los hilos para obtener la salida deseada, esto implica consumo de recursos físicos, lo que se traduce en tiempo de ejecución. Por 
	 esta razón puede que no se mejore el rendimiento de nuestro programana al ejecutarlo en 100 máquinas donde cada una corre un hilo. 
    
 ## 🐍 Snake Race 
 
 ### Part 1
 
   1. Download the project PrimeFinder. this is a program that calculates prime numbers beetween 0 and M (Control.MAXVALUE),concurrently, distributing the searching of them
   between n (Control.NTHREADS) independent threads.
   
   2. Modify the application in such way that each t milliseconds of thread execution, all the threads stop and show the number of primes found until that moment. Then, you
   have to wait until press ENTER in order to resume the threads execution.Use the synchronization mechanisms given by java (wait y notify, notifyAll).
   
   Note that:
   
   * The synchronized statement is used in order to get exclusive access to an object
      
   * The statement A.wait executed in a B thread set it to suspended mode (Independently that objective A is being used as 'lock') To resume, other active thread can
    resume B calling notify () to the object used as 'lock' ( in this case A)
    
   * The notify() statement, wakes the first thread  up who called wait() on the object
    
   * The notifyAll() instruction, wake  every thread up that are waiting for the object
   
 ### Part 2
   
   SnakeRace is an autonomous version, multi-snake of the famous game called 'snake' based on the Joao Andrade´s project-this exercise is a fork thereof
   
   * N snakes works as an autonomous way.
   
   * The collision concept does not exists among them. The only way that they die is by crashing against a wall
   
   * There are mice distributed along the game. As in the classic game, each time a snake eats  a mouse, it grows
   
   * There are some points (red arrows) that teleport the snakes

   * The rays causes that the snake increase its speed
    
     ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/SNAKERACE/SNAKE_RACE/img/sshot.png)
	 
  ### Part 3

 1. Analyse the code in order to understand how the threads are being used to create an autonomous behavior in the N snakes
	
 2. Accordingly, and using the game logic, identify and write clearly (ANSWERS.txt file)

	1. Possible race conditions
		
		  * Es posible que dos o más serpientes llegen al mismo tiempo a una celda donde hay un ratón, un flecha o un rayo, puede que ambas validen al mismo tiempo 
		  que hay comida allí y las dos crezcan, lo que no debería ser así ya que solo hay un ratón, solo una de las dos debería crecer. 
		  
		      **Solución:** Se ponen los métodos checkIfFood(Cell newCell), checkIfJumpPad(Cell newCell) y checkIfTurboBoost(Cell newCell) como sincronizados. 
		
 	2. An incorrect or inappropriate use of collections, considering its concurrent handling(For this increase the game speed and execute it multiples times until an
	error has been raised).
	
		  * La siguiente excepción puede ser lanzada por métodos que han detectado modificaciones concurrentes de un objeto cuando dicha modificación no está
		  permitida. Se hace un mal uso de la colección usada para representar el cuerpo de la serpiente, pues a medida que la serpiente se va a dibujando esta 
		  va creciendo, es decir se va modificando la lista.
		  
	          ![alt text](https://raw.githubusercontent.com/angipaola10/LAB1-ARSW/master/SNAKERACE/SNAKE_RACE/img/error.png)
		  

		      **Solución:** Cambiar el tipo de colección usada para almacenar los componentes del cuerpo de una serpiente por ConcurrentLinkedDeque<Cell>;
	
 	3. Unnecessary use of active waits
    
		  * Se presenta una espera innecesaria en el método init() SnakeApp, pues este usa un while para evaluar constantemente si las serpientes están vivas, cuando 
		   ya no queda ninguna serpiente viva imprime el estado de los hilos y termina el programa.
		  
		      **Solución:** En SnakeApp crear un contador para llevar el número de serpientes muertas y un método sincronizado para sumar uno a dicho contador, en
		      este método también se evalúa si el numéro de serpientes muertas alcanzó el numero máximo de hilos para imprimir el estado de ellos y finalizar el
		      programa. 
     
 3. Identify critical regions associated with race conditions, and do something in order to eliminate them.Note that you have to synchronize strictly needed. In the answers
 document suggest the solution proposed for each item of the point 2. As the same way note that you don´t have to add more race conditions
 
 4. As you can see, the game is incomplete. Write code in order to implement functionallities through buttons in the GUI to start / Pause / Resume the game: start the game if
 it has not started, Pause the game if it is on, Resume the game if it is suspended. Keep in mind:
 
    * When the game has been paused, in some point of the screen you have to show 
   
      1. the longest snake
      
      2. The worst snake:(the first snake  dead)
      
      Remember that the pause of the snakes are not instantanious, and you have to guarantee that all the information showed is consistent