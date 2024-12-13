package com.desafio2.biblioteca.service;

public interface IConvierteDatos {
	<T> T obtenerDatos(String json, Class<T> clase);
}
