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

import br.com.sistemaconsultorio.DAO.TipoCaracteristicaMaterialDAO;
import br.com.sistemaconsultorio.entities.Empresa;
import br.com.sistemaconsultorio.entities.TipoCaracteristicaMaterial;
import br.com.sistemaconsultorio.exceptions.AplicacaoException;
import br.com.sistemaconsultorio.exceptions.NegocioException;

/**
 * 
 * @author William Granzotto williamgranzotto@outlook.com
 * @date 19/11/2018 - 11:32:56
 *
 */
@Service(value = "tipoCaracteristicaMaterialService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TipoCaracteristicaMaterialService extends GenericService<TipoCaracteristicaMaterial>
		implements Serializable {

	private static final long serialVersionUID = -7685887338579354777L;

	@Autowired
	private TipoCaracteristicaMaterialDAO tipoCaracteristicaMaterialDAO;

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = AplicacaoException.class)
	@Override
	public TipoCaracteristicaMaterial incluir(TipoCaracteristicaMaterial entidade) throws Exception {
		try {
			return tipoCaracteristicaMaterialDAO.incluir(entidade);
		} catch (NegocioException e) {
			e.printStackTrace();
			throw new NegocioException(e.getMessage());
		} catch (AplicacaoException e) {
			e.printStackTrace();
			throw new Exception("Houve uma falha ao persistir Tipo Caracteristica Material: " + e.getMessage());
		}
	}

	/**
	 * @author William Granzotto williamgranzotto@outlook.com
	 * @date 19/11/2018 - 11:39:47
	 *
	 *       Lista todas as caracteristicas com base no ID da Empresa do usuario
	 *       logado
	 *
	 * @param empresaUsuarioLogado
	 * @return List<TipoCaracteristicaMaterial>
	 * @throws AplicacaoException
	 */
	public List<TipoCaracteristicaMaterial> obterTipoCaracteristicaMaterialPorEmpresa(Empresa empresaUsuarioLogado)
			throws AplicacaoException {
		return tipoCaracteristicaMaterialDAO.obterPorPropriedade("empresa.id", empresaUsuarioLogado.getId().toString(),
				"=", Boolean.FALSE, Boolean.FALSE);
	}

}
