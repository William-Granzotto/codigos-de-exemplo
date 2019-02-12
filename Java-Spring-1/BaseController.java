package br.com.sistemaconsultorio.controllers;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.sistemaconsultorio.security.Seguranca;
import br.com.sistemaconsultorio.utils.UtilFaces;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 18/11/2018 - 16:21:28
 *
 * @param <E>
 */
public abstract class BaseController<E> extends UtilFaces implements IBaseController, Serializable {

	private static final long serialVersionUID = -5599891370491150533L;

	private E bean;
	private E filtroBean;
	private List<E> filtrarDados;
	private List<E> tabelaDados;
	private String setarObjeto = StringUtils.EMPTY;

	@Autowired
	protected Seguranca seguranca;
	
	public E getBean() {
		return bean;
	}

	public void setBean(E bean) {
		this.bean = bean;
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		invalidateSession();
	}

	private void invalidateSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	public String showAlterar(String nome, String str) {
		UtilFaces.manipularObjetoNaSession(nome, getBean(), true);
		return str;
	}

	public E getFiltroBean() {
		return filtroBean;
	}

	public void setFiltroBean(E filtroBean) {
		this.filtroBean = filtroBean;
	}

	public List<E> getFiltrarDados() {
		return filtrarDados;
	}

	public void setFiltrarDados(List<E> filtrarDados) {
		this.filtrarDados = filtrarDados;
	}

	public List<E> getTabelaDados() {
		return tabelaDados;
	}

	public void setTabelaDados(List<E> tabelaDados) {
		this.tabelaDados = tabelaDados;
	}

	public String getSetarObjeto() {
		return setarObjeto;
	}

	public void setSetarObjeto(String setarObjeto) {
		this.setarObjeto = setarObjeto;
	}

	@Override
	public void initCad() throws Exception {
	}

	@Override
	public void initList() throws Exception {
	}

	@Override
	public String doDeleteAction() throws Exception {
		return null;
	}

	@Override
	public String doListAction() throws Exception {
		return null;
	}

	@Override
	public String doConsultAction() throws Exception {
		return null;
	}

	@Override
	public String showNovo() {
		return null;
	}

	@Override
	public String showPagina() {
		return null;
	}

	@Override
	public String doSaveAction() throws Exception {
		return null;
	}

	@Override
	public String doEditAction() throws Exception {
		return null;
	}

	@Override
	public String doCancelAction() throws Exception {
		return null;
	}
	
	/**
	 * 
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 20/11/2018 - 00:33:58
	 *
	 * Define atributos genericos do sweet 
	 *
	 * @param mensagem
	 */
	private void sweetAlertGeneric(String mensagem) {
		PrimeFaces.current().ajax().addCallbackParam("mensagem", mensagem);
		PrimeFaces.current().ajax().addCallbackParam("info", "Aviso!");
	}

	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @param string 
	 * @date 19/11/2018 - 16:27:06
	 *
	 * Exibe sweetalert com mensagem de sucesso
	 *
	 */
	public void dispararMensagemSweetAlertSucesso(String mensagem) {
		this.sweetAlertGeneric(mensagem);
		PrimeFaces.current().ajax().addCallbackParam("tipo", "success");
	}
	
	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 19/11/2018 - 16:30:57
	 *
	 * Exibe sweetalert com mensagem de campo obrigatório
	 *
	 */
	public void dispararMensagemSweetAlertCamposObrigatorios() {
		this.sweetAlertGeneric("Um ou mais campos de preenchimento obrigatorio ficou sem preencher.");
		PrimeFaces.current().ajax().addCallbackParam("tipo", "error");
	}

	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 19/11/2018 - 19:28:38
	 *
	 * Exibe sweetalert com mensagem de erro
	 *
	 * @param message
	 */
	public void dispararMensagemSweetAlertErro(String mensagem) {
		this.sweetAlertGeneric(mensagem);
		PrimeFaces.current().ajax().addCallbackParam("tipo", "error");
	}

	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 19/11/2018 - 20:53:04
	 *
	 * Força redirect para Index
	 *
	 * @return
	 */
	public String redirectIndex() {
		return "index.xhtml?faces-redirect=true";
	}

	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 20/11/2018 - 00:30:34
	 *
	 * Define um atributo exclusão para execução da exclusão
	 *
	 * @param string
	 */
	public void dispararMensagemSweetAlertExclusao(String mensagem) {
		this.sweetAlertGeneric(mensagem);
		PrimeFaces.current().ajax().addCallbackParam("exclusao", 1);
	}

}