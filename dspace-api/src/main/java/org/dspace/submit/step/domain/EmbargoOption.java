package org.dspace.submit.step.domain;

import java.util.EnumSet;

/**
 * Register embargo options
 * @author Márcio Ribeiro Gurgel do Amaral (marcio.rga@gmail.com)
 *
 */
public enum EmbargoOption {
	
	FREE(3, "Acesso aberto"),
	EMBARGOED(2, "Embargado"),
	RESTRICTED(1, "Restrito");
	
	private Integer id;
	private String key;
	
	private EmbargoOption(Integer id, String key) {
		this.id = id;
		this.key = key;
	}
	
	
	/**
	 * Recovers an instance of {@link EmbargoOption} by its ID
	 * @param id Id to be used as filer
	 * @return Found enum instance (or null)
	 */
	public static EmbargoOption recoverById(Integer id)
	{
		for(EmbargoOption embargoOption : EnumSet.allOf(EmbargoOption.class))
		{
			if(embargoOption.getId().equals(id))
			{
				return embargoOption;
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

	public Integer getId() {
		return id;
	}
}