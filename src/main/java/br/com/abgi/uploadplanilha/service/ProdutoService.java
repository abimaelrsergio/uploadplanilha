package br.com.abgi.uploadplanilha.service;

import java.util.List;
import java.util.Optional;

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

	public List<Produto> findAll() {
		return produtoRepository.findAll();
	}

	public Produto deleteById(int id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			produtoRepository.deleteById(id);
			return optional.get();
		}
		return null;
	}

	public Integer findById(Integer id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent())
			return optional.get().getId();
		return null;
	}
}
