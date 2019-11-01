package br.com.abgi.uploadplanilha.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Planilha implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6073520787222223767L;
	@Id
	@GeneratedValue
	private Integer id;
	private String path;
	private boolean processado;

	public Planilha() {
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isProcessado() {
		return processado;
	}

	public void setProcessado(boolean processado) {
		this.processado = processado;
	}

	@Override
	public String toString() {
		return "Planilha [id=" + id + ", path=" + path + ", processado=" + processado + "]";
	}
}
