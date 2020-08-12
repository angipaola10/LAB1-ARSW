# LABORATORIO 1
## Black List Search
### Parte 1
1. -
2. -
3. Se observa que la salida si cambia, esto se debe a que al iniciar un hilo con el método run() y luego
   iniciar otro hilo con el mismo método no se crea un proceso a parte, es decir se comportan de manera
   secuencial, hasta que el primer hilo iniciado no termine su ejecución no se inicia el segundo hilo; 
   mientras que si un hilo es iniciado con el método start() y luego se inicia otro hilo con el mismo 
   método si se crea un nuevo proceso y ambos hilos se ejecutan de forma concurrente, es por esto que en 
   la salida se alternan números de los diferentes rangos. 


