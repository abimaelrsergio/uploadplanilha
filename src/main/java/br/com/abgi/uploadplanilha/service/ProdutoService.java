package br.com.abgi.uploadplanilha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abgi.uploadplanilha.model.Produto;
import br.com.abgi.uploadplanilha.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public Produto save(Produto produto) {

		Produto produtoBase = produtoRepository.findByLm(produto.getLm());

		if (produtoBase != null)
			produto.setId(produtoBase.getId());

		return produtoRepository.save(produto);
	}
}
