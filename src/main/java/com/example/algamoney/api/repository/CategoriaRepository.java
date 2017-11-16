package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Categoria;

/*Repositório para a classe Categoria com a chave primária do tipo Long.
 * JpaRepository traz vários métodos prontos como saveAll, save, delete,
 * getOne etc.  */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
