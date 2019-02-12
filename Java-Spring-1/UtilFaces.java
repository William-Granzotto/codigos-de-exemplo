package br.com.sistemaconsultorio.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.sistemaconsultorio.enumerations.IEnum;
import br.com.sistemaconsultorio.enumerations.IEnumCodigo;
import br.com.sistemaconsultorio.exceptions.AplicacaoException;
import br.com.sistemaconsultorio.exceptions.NegocioException;

/**
 * 
 * @author William Granzotto - williamgranzotto@outlook.com 17 de nov de
 *         2018
 *
 */
public class UtilFaces implements Serializable {

	private static final long serialVersionUID = -5275459015893314326L;

	public static final DecimalFormat nf = new DecimalFormat("#,##0.00");
	public static final Integer QUERY_MAXIMO_RESULTADO = 100;
//	public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	static final Map<Locale, ResourceBundle> BUNDLE_MAP = new Hashtable<Locale, ResourceBundle>(2);

	public static void adicionarMensagemSucesso(final String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}

	public static void adicionarMensagemSucessoPelaChave(final String key, final String... details) {
		String msg = getMessage(key, details);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}

	public static void adicionarMensagemErro(final String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
	}

	public static void adicionarMensagemErro(final String msg, final String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, detail));
	}

	public static void redirect(final String path) throws AplicacaoException {
		try {
			final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect(context.getRequestContextPath() + path);
		} catch (final IOException e) {
			throw new AplicacaoException("Falha cr�tica do sistema");
		}
	}

	public static String getSessionId() {
		return UtilFaces.getSession().getId();
	}

	private static ResourceBundle getBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		Locale currentLocale = context.getViewRoot().getLocale();
		ResourceBundle bundle = BUNDLE_MAP.get(currentLocale);
		if (bundle == null) {
			bundle = (ResourceBundle) context.getApplication().evaluateExpressionGet(context, "#{msgs}",
					ResourceBundle.class);
			BUNDLE_MAP.put(currentLocale, bundle);
		}
		return bundle;
	}

	public static String getMessage(String key, String... details) {
		if (key != null) {
			String message = getBundle().getString(key);
			if (details != null && details.length > 0) {
				if (contaCaracter(message, '{') != details.length) {
					return null;
				} else {
					for (int i = 0; i < details.length; i++) {
						message = message.replace("{" + i + "}", details[i]);
					}
				}
			}
			return message;
		} else {
			return null;
		}
	}

	public static String getMensagemPorKey(String key) {
		if (key != null)
			return getBundle().getString(key);
		return "";
	}

	public static int contaCaracter(String texto, char caracter) {
		if (texto != null && caracter > 0) {
			int contador = 0;
			for (int i = 0; i < texto.length(); i++) {
				if (texto.charAt(i) == caracter) {
					contador++;
				}
			}
			return contador;
		} else {
			return -1;
		}
	}

	public static String getSequencia(Collection<?> colecao, String metodoGetValorSemParametros, String separador,
			int quebrarACadaCaracteres) throws NegocioException, AplicacaoException {

		if (colecao == null) {
			return "";
		}
		StringBuffer sequencia = new StringBuffer("");
		int quebras = 0;

		for (Object objeto : colecao) {
			sequencia.append(invocaMetodo(objeto, metodoGetValorSemParametros) + separador);
			if (quebrarACadaCaracteres > 0 && sequencia.length() >= quebrarACadaCaracteres
					&& (sequencia.length() / quebrarACadaCaracteres) > quebras) {
				sequencia.append("\n");
				quebras++;
			}
		}
		if (sequencia.length() >= separador.length()) {
			return sequencia.substring(0, sequencia.length() - separador.length());
		}
		return sequencia.toString();
	}

	public static String getSequenciaInsert(Collection<?> colecao, String metodoGetValorSemParametros, String separador,
			int quebrarACadaCaracteres) throws NegocioException, AplicacaoException {

		if (colecao == null) {
			return "";
		}
		StringBuffer sequencia = new StringBuffer("");
		int quebras = 0;

		for (Object objeto : colecao) {
			sequencia.append("'" + invocaMetodo(objeto, metodoGetValorSemParametros) + "'" + separador);
			if (quebrarACadaCaracteres > 0 && sequencia.length() >= quebrarACadaCaracteres
					&& (sequencia.length() / quebrarACadaCaracteres) > quebras) {
				sequencia.append("\n");
				quebras++;
			}
		}
		if (sequencia.length() >= separador.length()) {
			return sequencia.substring(0, sequencia.length() - separador.length());
		}
		return sequencia.toString();
	}

	public static Object invocaMetodo(Object bean, String nomeDoMetodo) throws NegocioException, AplicacaoException {
		Object objeto = null;
		try {
			String[] metodos = nomeDoMetodo.split("\\.");
			for (String nome : metodos) {
				Method metodo = bean.getClass().getMethod(nome);
				bean = metodo.invoke(bean);
			}
			objeto = bean;
		} catch (IllegalArgumentException e) {
			throw new AplicacaoException("argumento inv�lido ou ilegal ao invocar m�todo", e);
		} catch (IllegalAccessException e) {
			throw new AplicacaoException("sem acesso � execu��o de m�todo", e);
		} catch (InvocationTargetException e) {
			trataInvocationTargetException(e);
		} catch (SecurityException e) {
			throw new AplicacaoException("viola��o de seguran�a ao invocar m�todo", e);
		} catch (NoSuchMethodException e) {
			throw new AplicacaoException("m�todo n�o encontrado", e);
		}
		return objeto;
	}

	private static void trataInvocationTargetException(InvocationTargetException e)
			throws NegocioException, AplicacaoException {
		Throwable causa = e.getCause();
		while (causa != null) {
			if (causa instanceof NegocioException) {
				throw (NegocioException) causa;
			}
			causa = causa.getCause();
		}
		throw new AplicacaoException(e);
	}

	public String getRootDirectory(HttpServletRequest request) {
		return getRealPath(request, "");
	}

	public static String getRealPath(HttpServletRequest request, String path) {
		return getRealPath(request.getSession(), path);
	}

	public static String getRealPath(HttpSession session, String path) {
		return getRealPath(session.getServletContext(), path);
	}

	public static String getRealPath(ServletContext servletContext, String path) {
		return servletContext.getRealPath(path);
	}

	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	@SuppressWarnings("rawtypes")
	public static Map getSessionMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	}

	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	public static HttpServletRequest getServletRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static HttpServletResponse getServletResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public static HttpSession getSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session == null) {
			session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		}
		return session;
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static String obterParametroDaRequest(String nomeObjeto) {
		return getRequest().getParameter(nomeObjeto);
	}

	public static Object obterObjetoDaSession(String nomeObjeto) {
		return getSession().getAttribute(nomeObjeto);
	}

	public static void removerObjetoSession(String nomeObjeto) {
		getSession().removeAttribute(nomeObjeto);
	}

	public static void inserirObjetoNaSession(String nomeObjeto, Object event) {
		getSession().setAttribute(nomeObjeto, event);
	}

	public static void getResponseComplete() {
		FacesContext.getCurrentInstance().getResponseComplete();
	}

	public static void dispatch(String page) {

		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ec = ctx.getExternalContext();

		try {
			ec.dispatch(page);
		} catch (IOException ex) {
			adicionarMensagemErro(ex.getMessage());
		}

	}

	public static String getPathRaiz() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	}

	public static String getPath(String pasta) {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath(pasta);
	}

	public static Object getBeanManagerSession(String nameBeanManager) {
		return getSession().getAttribute(nameBeanManager);
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public static boolean isPostback() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	public static boolean isNotPostback() {
		return !isPostback();
	}

	public static String removerCaracteresEspeciais(String texto) {
		String textoSemCaracteres = Normalizer.normalize(texto, Normalizer.Form.NFD);
		return textoSemCaracteres.replaceAll("[^\\p{ASCII}]", "").replaceAll(" ", "_");
	}

	/**
	 * Formatar uma String exemplo: 1.111.111,1111 que cont�m valores monet�rios
	 * em um BigDecimal formatado (1111111.1111) para ser inserido ou atualizado no
	 * banco.
	 **/
	public static BigDecimal formatarValorMonetario(final String valor) {
		BigDecimal retorno = BigDecimal.ZERO;

		if (valor == null || valor.trim().equals("")) {
			return null;
		}
		// Valor vindo da tela --> 1.111.111.111,1111 para 1111111111.1111
		final String str = valor.replace('.', ' ');// retirando os pontos e
													// colocando espa�os em
													// branco
		final String numero = str.replace(" ", "");// retirando os espa�os em
													// branco
		final BigDecimal valorBanco = new BigDecimal(numero.replace(',', '.'));
		retorno = valorBanco;
		return retorno;
	}

	/**
	 * Receber um valor 1111111111.111 por exemplo, e formata para 1.111.111,1111
	 * 
	 **/
	public static String formatarValorMonetario(final BigDecimal valor) {
		if (valor != null) {
			DecimalFormat nf = new DecimalFormat("#,##0.00");
			return nf.format(valor.doubleValue());
		}
		return StringUtils.EMPTY;
	}

	public static Boolean isPreenchimentoNuloOuVazio(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof CharSequence) {
			return (((CharSequence) obj).length() == 0);
		} else if (obj instanceof Number) {
			return (((Number) obj).longValue() == 0);
		} else if (obj instanceof Collection<?>) {
			return (((Collection<?>) obj).isEmpty());
		} else if (obj instanceof Map) {
			return (((Map<?, ?>) obj).isEmpty());
		} else if (obj instanceof Object[]) {
			return (((Object[]) obj).length == 0);
		}
		return false;
	}

	public static String gerarCodigoVerificacao() {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ";
		Random random = new Random();
		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < 9; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}
		return armazenaChaves.toUpperCase();
	}

	@SuppressWarnings("unused")
	private String transformaListaString(List<String> lista) {
		String str = "";
		Iterator<String> it = lista.iterator();
		while (it.hasNext()) {
			str += it.next() + ",";
		}
		System.out.println(str.length());
		return str.substring(0, str.length() - 1);
	}

	public static void manipularObjetoNaSession(String descricao, Object objeto, Boolean inserir) {
		if (inserir)
			getSession().setAttribute(descricao, objeto);
		else
			getSession().removeAttribute(descricao);
	}

	public static String gerarStringAleatorio() {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ";
		Random random = new Random();
		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < 9; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}
		return armazenaChaves;
	}

	public static String gerarSenhaAleatorio() {
		String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ";
		Random random = new Random();
		String armazenaChaves = "";
		int index = -1;
		for (int i = 0; i < 4; i++) {
			index = random.nextInt(letras.length());
			armazenaChaves += letras.substring(index, index + 1);
		}
		return armazenaChaves;
	}

	public static byte[] converteFileEmBytes(File file) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileInputStream in = new FileInputStream(file);
		int b;
		while ((b = in.read()) > -1) {
			out.write(b);
		}
		out.close();
		in.close();
		return out.toByteArray();
	}

	public static String formatar(String valor, String mascara) {

		String dado = "";
		for (int i = 0; i < valor.length(); i++) {
			char c = valor.charAt(i);
			if (Character.isDigit(c)) {
				dado += c;
			}
		}

		int indMascara = mascara.length();
		int indCampo = dado.length();

		for (; indCampo > 0 && indMascara > 0;) {
			if (mascara.charAt(--indMascara) == '#') {
				indCampo--;
			}
		}

		String saida = "";
		for (; indMascara < mascara.length(); indMascara++) {
			saida += ((mascara.charAt(indMascara) == '#') ? dado.charAt(indCampo++) : mascara.charAt(indMascara));
		}
		return saida;
	}

	public static String formatarCpf(String cpf) {
		while (cpf.length() < 11) {
			cpf = "0" + cpf;
		}
		return formatar(cpf, "###.###.###-##");
	}

	public static String formatarCnpj(String cnpj) {
		while (cnpj.length() < 14) {
			cnpj = "0" + cnpj;
		}
		return formatar(cnpj, "##.###.###/####-##");
	}

	public static String formatarCpfCnpj(String str) {
		if (str.length() > 11) {
			return formatarCnpj(str);
		}
		return formatarCpf(str);
	}

	public static String removerCaracter(final String str, final char c) {
		final StringBuffer new_str = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != c) {
				new_str.append(str.charAt(i));
			}
		}
		return new_str.toString();
	}

	public static String removerEspacoString(final String arg) throws Exception {
		String res = "";
		try {
			if (arg != null) {
				final String padrao = "\\s{2,}";
				final Pattern regPat = Pattern.compile(padrao);
				final Matcher matcher = regPat.matcher(arg);
				res = matcher.replaceAll(" ").trim();
			}
		} catch (final Exception e) {
			UtilLog.gerarLog(UtilFaces.class, e);
		}
		if (res.trim().equals("")) {
			return null;
		}
		return res;
	}

	public static String somenteNumeros(String str) {
		return str.replaceAll("[^0-9]*", "");
	}

	/**
	 * Retorna o Hash MD5 da string de entrada. na fun��o getBytes foi passado o
	 * parametro (ISO-8859-1) como especifica��o da tabela de caracteres pois a
	 * sobrecarga da fun��o que n�o tem parametros, usa um tipo de codifica��o
	 * diferente para cada sistema operacional.
	 *
	 * @param str
	 *            - String a ser criptografada
	 * @return hexString - String criptografada
	 * @throws NoSuchAlgorithmException,
	 *             UnsupportedEncodingException
	 */
	public static String encrypt(final String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes("ISO-8859-1"));

		final byte[] hash = md.digest();
		final StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < hash.length; i++) {
			if ((0xff & hash[i]) < 0x10) {
				hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
			} else {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
		}

		return hexString.toString();
	}

	public static void setFocusComponente(String idComponente) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getRequestMap().put("focus", idComponente);

	}

	public static Object getValorParametro(ActionEvent evento, String idParametro) {
		try {
			UIParameter param = (UIParameter) evento.getComponent().findComponent(idParametro);
			return param.getValue();
		} catch (Exception e) {
			throw new RuntimeException("Par�metro " + idParametro + " nao encontrado");
		}
	}

	public static List<SelectItem> getSelectItemEnumCodigo(IEnumCodigo[] enuns) {
		List<SelectItem> resultado = new ArrayList<SelectItem>();
		for (IEnumCodigo tipo : enuns) {
			resultado.add(new SelectItem(tipo, tipo.getDescricao()));
		}
		return resultado;
	}

	public static List<SelectItem> getSelectItemCodigoEnum(IEnumCodigo[] enuns) {
		List<SelectItem> resultado = new ArrayList<SelectItem>();
		for (IEnumCodigo tipo : enuns) {
			resultado.add(new SelectItem(tipo.getStatus().longValue(), tipo.getDescricao()));
		}
		return resultado;
	}

	public static List<SelectItem> getSelectItemEnumStatus(IEnum[] enuns) {
		List<SelectItem> resultado = new ArrayList<SelectItem>();
		for (IEnum tipo : enuns) {
			resultado.add(new SelectItem(tipo, tipo.getStatus()));
		}
		return resultado;
	}

	public static List<SelectItem> getSelectItemEnum(IEnum[] enuns) {
		List<SelectItem> resultado = new ArrayList<SelectItem>();
		for (IEnum tipo : enuns) {
			resultado.add(new SelectItem(tipo.getStatus(), tipo.getDescricao()));
		}
		return resultado;
	}

	public static void isValidaPreenchimentoLista(Collection<?> lista, String nomeDaLista) {
		if (isPreenchimentoNuloOuVazio(lista)) {
			throw new NegocioException("Favor preencher a lista de " + nomeDaLista);
		}
	}

	public static void isValidaPreenchimentoObjeto(Object objeto, String nomeObjeto) {
		if (isPreenchimentoNuloOuVazio(objeto)) {
			throw new NegocioException("Favor, selecione um " + nomeObjeto);
		}
	}

	public static Long gerarUnicoId() {
		long val = -1;
		do {
			final UUID uid = UUID.randomUUID();
			final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
			buffer.putLong(uid.getLeastSignificantBits());
			buffer.putLong(uid.getMostSignificantBits());
			final BigInteger bi = new BigInteger(buffer.array());
			val = bi.longValue();
		} while (val < 0);
		return val;
	}

	

	public static String recuperaDiretorioRaiz() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		ServletContext ctx = (ServletContext) externalContext.getContext();
		return ctx.getRealPath("/");
	}

	public static byte[] obterBytesFile(UploadedFile file) throws IOException {
		return (IOUtils.toByteArray(file.getInputstream()));
	}

	public StreamedContent converteBytesEmFile(byte[] file) throws IOException {
		return new DefaultStreamedContent(new ByteArrayInputStream(file));
	}

}
