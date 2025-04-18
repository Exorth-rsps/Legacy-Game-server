package org.alter.plugins.content.mechanics.shops

import org.alter.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CoinCurrency : ItemCurrency(Items.COINS_995, singularCurrency = "coin", pluralCurrency = "coins")
class UnidentifiedMineralsCurrency : ItemCurrency(Items.UNIDENTIFIED_MINERALS, singularCurrency = "unidentified mineral", pluralCurrency = "unidentified minerals")
class AnimainfusedbarkCurrency : ItemCurrency(Items.ANIMAINFUSED_BARK, singularCurrency = "anima-infused bark", pluralCurrency = "anima-infused barks")