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

        if (stack.isEmpty()) {
            // Inicializa el tamaño de los subsegmentos en 1
            stack.push(new int[]{1, array.length});
        }

        while (!stack.isEmpty()) {
            if ((System.currentTimeMillis() - startTime) >= timeLimitMillis) {
                acomulatedTime += System.currentTimeMillis() - startTime;
                break;
            }

            // Obtiene el tamaño actual de los subsegmentos y el tamaño total del arreglo
            int[] range = stack.pop();
            int currentSize = range[0];
            int totalSize = range[1];

            if (currentSize < totalSize) {
                // Fusiona subsegmentos de tamaño `currentSize`
                for (int left = 0; left < totalSize - currentSize; left += 2 * currentSize) {
                    int mid = left + currentSize - 1;
                    int right = Math.min(left + 2 * currentSize - 1, totalSize - 1);

                    merge(left, mid, right);
                }

                // Incrementa el tamaño de los subsegmentos y vuelve a procesar
                stack.push(new int[]{currentSize * 2, totalSize});
            } else {
                // Finaliza cuando el tamaño del subsegmento cubre todo el arreglo
                acomulatedTime += System.currentTimeMillis() - startTime;
                finished = true;
                break;
            }
        }
    }

    private void merge(int left, int mid, int right) {
        int[] tempArray = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        // Combina elementos de ambas mitades
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                tempArray[k++] = array[i++];
            } else {
                tempArray[k++] = array[j++];
            }
        }

        // Copia los elementos restantes de la primera mitad
        while (i <= mid) {
            tempArray[k++] = array[i++];
        }

        // Copia los elementos restantes de la segunda mitad
        while (j <= right) {
            tempArray[k++] = array[j++];
        }

        // Copia el arreglo temporal de vuelta al original
        System.arraycopy(tempArray, 0, array, left, tempArray.length);
    }

    public void startHeapSort(int t) {
        this.timeLimitMillis = t * 1000;
        this.startTime = System.currentTimeMillis();

        if (stack.isEmpty()) {
            // Construcción del montón inicial
            for (int i = array.length / 2 - 1; i >= 0; i--) {
                stack.push(new int[]{i, array.length - 1});
            }
        }

        while (!stack.isEmpty()) {
            if ((System.currentTimeMillis() - startTime) >= timeLimitMillis) {
                acomulatedTime += System.currentTimeMillis() - startTime;
                break;
            }

            int[] range = stack.pop();
            int i = range[0];
            int n = range[1];

            heapify(n, i);

            if (stack.isEmpty() && n > 0) {
                // Intercambiar raíz con el último elemento
                swap(0, n - 1);
                stack.push(new int[]{0, n - 2}); // Reconstruir el montón con rango reducido
            }

            if (stack.isEmpty()) {
                acomulatedTime += System.currentTimeMillis() - startTime;
                finished = true; // Marca como terminado
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
