package mod.emt.legendgear.spell;

import java.util.HashMap;
import java.util.Map;

// TODO: Spells still need proper attributes and balancing
public class LGSpellRegistry
{
    private static final Map<String, LGSpell> SPELLS = new HashMap<>();

    //public static final LGSpell EARTH = register(new LGSpellEarth("ground_quake"));
    public static final LGSpell ENDER = register(new LGSpellEnder("exeunt"));
    public static final LGSpell FIRE = register(new LGSpellFire("ember"));
    public static final LGSpell ICE = register(new LGSpellIce("frost"));
    public static final LGSpell LIGHTNING = register(new LGSpellLightning("jolt"));
    public static final LGSpell PHOENIX = register(new LGSpellPhoenix("rayfire"));
    public static final LGSpell SPIRIT = register(new LGSpellSpirit("spirit_drain"));
    public static final LGSpell STELLAR = register(new LGSpellStellar("twinkle"));
    public static final LGSpell WIND = register(new LGSpellWind("scythewind"));

    private static LGSpell register(LGSpell spell)
    {
        SPELLS.put(spell.getId(), spell);
        return spell;
    }

    public static LGSpell get(String id)
    {
        return SPELLS.get(id);
    }
}
