package br.com.sistemaconsultorio.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.sistemaconsultorio.entities.CaracteristicasMaterial;
import br.com.sistemaconsultorio.entities.Material;
import br.com.sistemaconsultorio.entities.TipoCaracteristicaMaterial;
import br.com.sistemaconsultorio.entities.TipoMaterial;
import br.com.sistemaconsultorio.exceptions.AplicacaoException;
import br.com.sistemaconsultorio.services.MaterialService;
import br.com.sistemaconsultorio.services.TipoCaracteristicaMaterialService;
import br.com.sistemaconsultorio.services.TipoMaterialService;
import br.com.sistemaconsultorio.utils.UtilFaces;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 18/11/2018 - 16:13:34
 *
 */
@Controller
@Scope("view")
public class MaterialController extends BaseController<Material> implements Serializable {

	private static final long serialVersionUID = -7337545361139992499L;

	private CaracteristicasMaterial caracteristicasMaterial = new CaracteristicasMaterial();
	private List<TipoMaterial> listaTipoMaterial = new ArrayList<>();
	private List<TipoCaracteristicaMaterial> listaTipoCaracteristicaMaterial = new ArrayList<>();
	private Long posicao = -1L;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private TipoMaterialService tipoMaterialService;

	@Autowired
	private TipoCaracteristicaMaterialService tipoCaracteristicaMaterialService;

	@Autowired
	private Paginacao paginacao;

	@Override
	public String showPagina() {
		return super.showPagina();
	}

	@Override
	public void initList() throws Exception {
		if (UtilFaces.isNotPostback()) {
			setBean(new Material());
			setTabelaDados(this.materialService.obterMateriaisPorEmpresa(seguranca.getEmpresaUsuarioLogado()));
		}
	}

	@PostConstruct
	@Override
	public void initCad() throws Exception {
		setBean(new Material());
		if (UtilFaces.isNotPostback()) {
			setTabelaDados(this.materialService.obterMateriaisPorEmpresa(seguranca.getEmpresaUsuarioLogado()));
			;
		}
		this.atualizarListaTipoMateriais();
		this.listaTipoCaracteristicaMaterial = tipoCaracteristicaMaterialService
				.obterTipoCaracteristicaMaterialPorEmpresa(seguranca.getEmpresaUsuarioLogado());
	}

	@Override
	public String doSaveAction() throws Exception {
		try {
			if (this.materialService.validarCamposObrigatorios(getBean())) {
				dispararMensagemSweetAlertCamposObrigatorios();
				return null;
			}
			getBean().setEmpresa(seguranca.getEmpresaUsuarioLogado());
			getBean().setOperacao(seguranca.getOperacao());
			materialService.incluir(getBean());
			setBean(new Material());
			dispararMensagemSweetAlertSucesso("Material gravado com sucesso");
			return null;
		} catch (Exception e) {
			dispararMensagemSweetAlertErro(e.getMessage());
		}
		return null;
	}

	@Override
	public String doDeleteAction() throws Exception {
		try {
			this.materialService.excluirPorId(getBean());
			super.dispararMensagemSweetAlertExclusao("Material excluído com sucesso!");
			setBean(new Material());
			return null;
		} catch (Exception e) {
			dispararMensagemSweetAlertErro(e.getMessage());
		}
		return null;
	}

	@Override
	public String doEditAction() throws Exception {
		paginacao.setPaginaAtual("materiais");
		return null;
	}

	public void atualizarListaTipoMateriais() throws AplicacaoException {
		this.listaTipoMaterial = this.tipoMaterialService
				.obterTipoMaterialPorEmpresa(seguranca.getEmpresaUsuarioLogado());
	}

	public void adicionarCaracteristicaMaterial() {
		if (posicao >= 0) {
			this.caracteristicasMaterial = this.getBean().getListaCaractMaterial().get(posicao.intValue());
			if (caracteristicasMaterial != null) {
				this.caracteristicasMaterial.setEmpresa(seguranca.getEmpresaUsuarioLogado());
				this.caracteristicasMaterial.setMaterial(this.getBean());
				this.getBean().getListaCaractMaterial().set(posicao.intValue(), this.caracteristicasMaterial);
			}
		}else {
			this.caracteristicasMaterial.setEmpresa(seguranca.getEmpresaUsuarioLogado());
			this.caracteristicasMaterial.setMaterial(this.getBean());
			this.getBean().getListaCaractMaterial().add(this.caracteristicasMaterial);
		}
		this.caracteristicasMaterial = new CaracteristicasMaterial().get();
		this.posicao = -1L;
	}
	
	public void excluirCaracteristicaMaterial() {
		if (posicao >= 0) {
			this.caracteristicasMaterial = this.getBean().getListaCaractMaterial().get(this.posicao.intValue());
			this.caracteristicasMaterial.setMaterial(null);
			this.getBean().getListaCaractMaterial().remove(this.posicao.intValue());
			dispararMensagemSweetAlertSucesso("Caracteristica excluida com sucesso!");
			this.caracteristicasMaterial = new CaracteristicasMaterial().get();
		}
		this.posicao = -1L;
	}
	
	@Deprecated
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        System.out.println(oldValue + "-" + newValue);
    }

	/**
	 * @return the listaTipoMaterial
	 */
	public List<TipoMaterial> getListaTipoMaterial() {
		return listaTipoMaterial;
	}

	/**
	 * @param listaTipoMaterial
	 *            the listaTipoMaterial to set
	 */
	public void setListaTipoMaterial(List<TipoMaterial> listaTipoMaterial) {
		this.listaTipoMaterial = listaTipoMaterial;
	}

	/**
	 * @return the listaTipoCaracteristicaMaterial
	 */
	public List<TipoCaracteristicaMaterial> getListaTipoCaracteristicaMaterial() {
		return listaTipoCaracteristicaMaterial;
	}

	/**
	 * @param listaTipoCaracteristicaMaterial
	 *            the listaTipoCaracteristicaMaterial to set
	 */
	public void setListaTipoCaracteristicaMaterial(List<TipoCaracteristicaMaterial> listaTipoCaracteristicaMaterial) {
		this.listaTipoCaracteristicaMaterial = listaTipoCaracteristicaMaterial;
	}

	/**
	 * @return the caracteristicasMaterial
	 */
	public CaracteristicasMaterial getCaracteristicasMaterial() {
		return caracteristicasMaterial;
	}

	/**
	 * @param caracteristicasMaterial
	 *            the caracteristicasMaterial to set
	 */
	public void setCaracteristicasMaterial(CaracteristicasMaterial caracteristicasMaterial) {
		this.caracteristicasMaterial = caracteristicasMaterial;
	}

	/**
	 * @return the posicao
	 */
	public Long getPosicao() {
		return posicao;
	}

	/**
	 * @param posicao the posicao to set
	 */
	public void setPosicao(Long posicao) {
		this.posicao = posicao;
	}
	
	

}
