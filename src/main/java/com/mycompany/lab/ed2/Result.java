/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab.ed2;

import java.io.Serializable;

public class Result implements Serializable {
    private int[] vector;
    private long time;

    public Result(int[] vector, long time) {
        this.vector = vector;
        this.time = time;
    }

    public int[] getVector() {
        return vector;
    }

    public long getTime() {
        return time;
    }
}
