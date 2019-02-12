package br.com.sistemaconsultorio.controllers;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.sistemaconsultorio.entities.TipoCaracteristicaMaterial;
import br.com.sistemaconsultorio.exceptions.NegocioException;
import br.com.sistemaconsultorio.services.TipoCaracteristicaMaterialService;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 19/11/2018 - 11:36:59
 *
 */
@Controller
@Scope("view")
public class TipoCaracteristicaMaterialController extends BaseController<TipoCaracteristicaMaterial>
		implements Serializable {

	private static final long serialVersionUID = -3967850746099851795L;

	@Autowired
	private TipoCaracteristicaMaterialService tipoCaracteristicaMaterialService;

	@Override
	public void initCad() throws Exception {
		setBean(new TipoCaracteristicaMaterial());
	}

	@Override
	public String doSaveAction() throws Exception {
		try {
			getBean().setEmpresa(seguranca.getEmpresaUsuarioLogado());
			getBean().setOperacao(seguranca.getOperacao());
			tipoCaracteristicaMaterialService.incluir(getBean());
			adicionarMensagemSucesso("Tipo Caracteristica Material inserido com sucesso!");
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
