# LABORATORIO 1
  📌 **Angi Paola Jiménez Pira**
## Black List Search
### Part I - Introduction to threads in JAVA
 
**3.** 
   Se observa que la salida cambia, esto se debe a que cuando se ejecuta `thread1.run()`, lo que sucede es que se llama directamente al método `run()` de thread1 el cual es un
   método secuencial normal, por lo tanto, hasta que no termine de ejecutarse `thread1.run()` no se ejecutará `thread2.run()`; por otro lado, si se ejecuta `thread1.start()` se
   crea el hilo y si luego se llama a `thread2.start()` se crea otro hilo y estos se ejecutan paralelamente.
   
### Part II - Black List Search Exercise



