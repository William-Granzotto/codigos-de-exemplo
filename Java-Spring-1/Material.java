/**
 * 
 */
package br.com.sistemaconsultorio.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 17/11/2018 - 20:39:41
 *
 */
@Entity
public class Material implements Serializable {

	private static final long serialVersionUID = -640968989307746602L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "quantidade")
	private Long quantidade;

	@Column(name = "valorcusto")
	private BigDecimal valorCusto;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name = "id_tipomaterial")
	private TipoMaterial tipoMaterial;

	@Column(name="marca")
	private String marca;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "material", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CaracteristicasMaterial> listaCaractMaterial = new ArrayList<>();

	@Embedded
	private Operacao operacao;

	public Material() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the quantidade
	 */
	public Long getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade
	 *            the quantidade to set
	 */
	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the valorCusto
	 */
	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	/**
	 * @param valorCusto
	 *            the valorCusto to set
	 */
	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	/**
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the tipoMaterial
	 */
	public TipoMaterial getTipoMaterial() {
		return tipoMaterial;
	}

	/**
	 * @param tipoMaterial
	 *            the tipoMaterial to set
	 */
	public void setTipoMaterial(TipoMaterial tipoMaterial) {
		this.tipoMaterial = tipoMaterial;
	}

	/**
	 * @return the listaCaractMaterial
	 */
	public List<CaracteristicasMaterial> getListaCaractMaterial() {
		return listaCaractMaterial;
	}

	/**
	 * @param listaCaractMaterial
	 *            the listaCaractMaterial to set
	 */
	public void setListaCaractMaterial(List<CaracteristicasMaterial> listaCaractMaterial) {
		this.listaCaractMaterial = listaCaractMaterial;
	}

	/**
	 * @return the operacao
	 */
	public Operacao getOperacao() {
		return operacao;
	}

	/**
	 * @param operacao
	 *            the operacao to set
	 */
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}

	/**
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * @param marca the marca to set
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Material other = (Material) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
