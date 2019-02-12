package br.com.sistemaconsultorio.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.sistemaconsultorio.entities.TipoMaterial;
import br.com.sistemaconsultorio.exceptions.NegocioException;
import br.com.sistemaconsultorio.services.TipoMaterialService;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 18/11/2018 - 16:15:11
 *
 */
@Controller
@Scope("view")
public class TipoMaterialController extends BaseController<TipoMaterial> implements Serializable {

	private static final long serialVersionUID = -3967850746099851795L;

	@Autowired
	private TipoMaterialService tipoMaterialService;
	
	@PostConstruct
	@Override
	public void initCad() throws Exception {
		setBean(new TipoMaterial());
	}
	
	@Override
	public String doSaveAction() throws Exception {
		try {
			if (this.tipoMaterialService.validarCamposObrigatorios(getBean())) {
				dispararMensagemSweetAlertCamposObrigatorios();
				return null;
			}
			getBean().setEmpresa(seguranca.getEmpresaUsuarioLogado());
			getBean().setOperacao(seguranca.getOperacao());
			this.tipoMaterialService.incluir(getBean());
			setBean(new TipoMaterial());
			dispararMensagemSweetAlertSucesso("Tipo Material inserido com sucesso!");
			return null;
		} catch (NegocioException e) {
			adicionarMensagemErro(e.getMessage());
		} catch (Exception e) {
			adicionarMensagemErro(e.getMessage());
		}
		return null;
	}

	@Override
	public String doDeleteAction() throws Exception {
		return super.doDeleteAction();
	}

}
