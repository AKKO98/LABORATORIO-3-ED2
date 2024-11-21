/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab.ed2;

import java.io.Serializable;
import java.util.Stack;

/**
 *
 * @author bazas
 */
public class Sorter implements Serializable {

    private static final long serialVersionUID = 1L;
    private volatile int[] array;           // Array a ordenar
    private volatile boolean finished = false; // Estado del algoritmo
    private long acomulatedTime = 0;
    private long timeLimitMillis;              // Límite de tiempo en milisegundos

    // Variables de control para el algoritmo iterativo
    private Stack<int[]> stack = new Stack<>(); // Pila para manejar particiones
    private long startTime;                    // Tiempo inicial de ejecución

    // Variables globales para MergeSort
    private volatile int[] tempArray;  // Arreglo temporal para fusiones
    private volatile int left, right, mid; // Índices usados durante la fusión

    public Sorter(int[] array) {
        this.array = array;
        finished = false;
    }

    public long getAcomulatedTime() {
        return acomulatedTime;
    }

    public void startQuickSort(int t) {
        // Inicializa el tiempo límite
        this.timeLimitMillis = t * 1000;
        this.startTime = System.currentTimeMillis();

        // Si la pila está vacía, inicia la partición completa
        if (stack.isEmpty()) {
            stack.push(new int[]{0, array.length - 1});
        }

        // Ejecuta el QuickSort iterativo
        while (!stack.isEmpty()) {
            if ((System.currentTimeMillis() - startTime) >= timeLimitMillis) {
                // Si se alcanza el tiempo límite, pausa
                acomulatedTime = +t * 1000;
                break;
            }

            int[] range = stack.pop();
            int low = range[0];
            int high = range[1];

            if (low < high) {
                int pivotIndex = partition(low, high);

                // Agrega las nuevas particiones a la pila
                stack.push(new int[]{low, pivotIndex - 1});
                stack.push(new int[]{pivotIndex + 1, high});
            }
            if (stack.isEmpty()) {
                acomulatedTime += System.currentTimeMillis() - startTime;
                finished = true; // Marca como terminado si la pila está vacía
                break;
            }
        }


    }

    public void resumeQuickSort() {
        startQuickSort((int) timeLimitMillis / 1000);
    }

    public void resumeMergeSort() {
        startMergeSort((int) timeLimitMillis / 1000);
    }

    public void resumeHeapSort() {
        startHeapSort((int) timeLimitMillis / 1000);
    }

    public boolean isFinished() {
        return finished;
    }

    private int partition(int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public void startMergeSort(int t) {
        this.timeLimitMillis = t * 1000;
        this.startTime = System.currentTimeMillis();

        // Inicializa el arreglo temporal y las particiones si es la primera ejecución
        if (tempArray == null) {
            tempArray = new int[array.length];
            stack.push(new int[]{0, array.length - 1}); // Empieza con todo el arreglo
        }

        // Iterativo usando la pila
        while (!stack.isEmpty()) {
            if ((System.currentTimeMillis() - startTime) >= timeLimitMillis) {
                acomulatedTime += t * 1000;
                break;
            }

            int[] range = stack.pop();
            left = range[0];
            right = range[1];

            if (left < right) {
                mid = left + (right - left) / 2;

                // Primero procesa las particiones inferiores
                stack.push(new int[]{left, right});     // Marca para fusión
                stack.push(new int[]{mid + 1, right}); // Derecha
                stack.push(new int[]{left, mid});      // Izquierda
            } else {
                // Fusiona los rangos cuando ya están particionados
                if (!stack.isEmpty() && stack.peek()[0] == left && stack.peek()[1] == right) {
                    stack.pop(); // Marca como fusionado
                    merge();
                }
            }
            if (stack.isEmpty()) {
                finished = true; // Marca como terminado si la pila está vacía
                tempArray = null; // Libera el arreglo temporal
                acomulatedTime += System.currentTimeMillis() - startTime;
                break;
            }
        }
    }

// Método auxiliar para fusionar
    private void merge() {
        // Copia el rango actual al arreglo temporal
        for (int i = left; i <= right; i++) {
            tempArray[i] = array[i];
        }

        int i = left, j = mid + 1, k = left;

        // Fusión de dos subarreglos ordenados
        while (i <= mid && j <= right) {
            if (tempArray[i] <= tempArray[j]) {
                array[k++] = tempArray[i++];
            } else {
                array[k++] = tempArray[j++];
            }
        }

        // Copia los elementos restantes del lado izquierdo
        while (i <= mid) {
            array[k++] = tempArray[i++];
        }

        // Los elementos del lado derecho ya están en su lugar
    }

    public void startHeapSort(int t) {
        this.timeLimitMillis = t * 1000;
        this.startTime = System.currentTimeMillis();

        if (stack.isEmpty()) {
            // Paso 1: Construcción del montón
            for (int i = array.length / 2 - 1; i >= 0; i--) {
                stack.push(new int[]{i, array.length - 1}); // Rango [i, n-1] para aplicar heapify
            }
        }

        while (!stack.isEmpty()) {
            if ((System.currentTimeMillis() - startTime) >= timeLimitMillis) {
                acomulatedTime += t * 1000;
                break;
            }

            int[] range = stack.pop();
            int i = range[0];
            int n = range[1];

            heapify(n, i);

            if (stack.isEmpty() && n > 0) {
                // Paso 2: Intercambio y reducción del montón
                swap(0, n);
                stack.push(new int[]{0, n - 1}); // Reconstruir el montón para el rango reducido
            }
            if (stack.isEmpty()) {
                acomulatedTime += System.currentTimeMillis() - startTime;
                finished = true; // Marca como terminado si la pila está vacía
                break;
            }
        }

    }

// Método auxiliar para mantener la propiedad del montón
    private void heapify(int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(i, largest);
            heapify(n, largest); // Aplicar heapify en el subárbol afectado
        }
    }

    public int[] getArray() {
        return array;
    }

}
