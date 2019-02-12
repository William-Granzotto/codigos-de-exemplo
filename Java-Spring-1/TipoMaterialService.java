package br.com.sistemaconsultorio.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.sistemaconsultorio.DAO.TipoMaterialDAO;
import br.com.sistemaconsultorio.entities.Empresa;
import br.com.sistemaconsultorio.entities.TipoMaterial;
import br.com.sistemaconsultorio.exceptions.AplicacaoException;
import br.com.sistemaconsultorio.exceptions.NegocioException;
import br.com.sistemaconsultorio.utils.UtilFaces;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 18/11/2018 - 16:16:33
 *
 */
@Service(value = "tipoMaterialService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TipoMaterialService extends GenericService<TipoMaterial> implements Serializable {

	private static final long serialVersionUID = 142374572732911479L;

	@Autowired
	private TipoMaterialDAO tipoMaterialDAO;

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AplicacaoException.class)
	@Override
	public TipoMaterial incluir(TipoMaterial entidade) throws Exception {
		try {
			return tipoMaterialDAO.incluir(entidade);
		} catch (NegocioException e) {
			e.printStackTrace();
			throw new NegocioException(e.getMessage());
		} catch (AplicacaoException e) {
			e.printStackTrace();
			throw new Exception("Houve uma falha ao persistir Tipo Material: " + e.getMessage());
		}
	}

	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 19/11/2018 - 10:29:54
	 *
	 *       Lista todos os materiais com base no ID da Empresa do usuario
	 *       logado
	 *
	 * @param empresaUsuarioLogado
	 * @return List<TipoMaterial>
	 * @throws AplicacaoException
	 */
	public List<TipoMaterial> obterTipoMaterialPorEmpresa(Empresa empresaUsuarioLogado) throws AplicacaoException {
		return tipoMaterialDAO.obterPorPropriedade("empresa.id", empresaUsuarioLogado.getId().toString(), "=",
				Boolean.FALSE, Boolean.FALSE);
	}

	public boolean validarCamposObrigatorios(TipoMaterial bean) {
		if (UtilFaces.isPreenchimentoNuloOuVazio(bean)) {
			return Boolean.TRUE;
		}else if (UtilFaces.isPreenchimentoNuloOuVazio(bean.getNome())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
