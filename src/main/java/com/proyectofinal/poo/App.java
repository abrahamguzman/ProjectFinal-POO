package com.proyectofinal.poo;

import com.proyectofinal.poo.repository.ToolRepository;


public class App {
    public static void main(String[] args) {
        ToolRepository toolRepository = new ToolRepository();
        toolRepository.findAll().forEach(System.out::println);
    }
}
