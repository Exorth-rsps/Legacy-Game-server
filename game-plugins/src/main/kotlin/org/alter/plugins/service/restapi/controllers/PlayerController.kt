class PlayerController(req: Request, resp: Response, auth: Boolean) : Controller(req, resp, auth) {
    private val logger = KotlinLogging.logger {}
    private val gson = Gson()

    override fun init(world: World): JsonObject {
        val obj = JsonObject()
        val userArr = JsonArray()
        val username = req.params("name")

        // 1) Probeer online speler
        val online = world.players.find { it.username.equals(username, ignoreCase = true) }
        if (online != null) {
            userArr.add(buildPlayerObject(online.username, online.privilege.id,
                online.gameMode, online.combatLevel, online.isOnline,
                online.xpRate, online.uid.toString(),
                world, online.getSkills()))
        } else {
            // 2) Fallback: probeer save-bestand in data/saves/<username>.json
            val saveFile = resolveSavesDir()?.resolve("$username.json")
            if (saveFile != null && Files.exists(saveFile)) {
                try {
                    val jo = gson.fromJson(Files.newBufferedReader(saveFile), JsonObject::class.java)
                    val privilege = jo.get("privilege").asInt
                    val gameMode  = jo.get("displayMode")?.asInt ?: 0
                    val combatLvl = jo.get("combatLvl")?.asInt ?: jo.get("combatLevel")?.asInt ?: 0
                    val isOnline  = false
                    val xpRate    = 0
                    val uid       = jo.get("username").asString

                    // Skills uit save-JSON (array van {skill, xp, lvl})
                    val skillsArray = jo.getAsJsonArray("skills")
                    userArr.add(buildPlayerObject(
                        username, privilege, gameMode, combatLvl, isOnline,
                        xpRate, uid, null, skillsArray
                    ))
                } catch (e: Exception) {
                    logger.error(e) { "Kon save $saveFile niet inlezen" }
                }
            }
        }

        obj.add("player", userArr)
        return obj
    }

    /** Zet data om in JsonObject */
    private fun buildPlayerObject(
        username: String,
        privilege: Int,
        gameMode: Int,
        combatLvl: Int,
        isOnline: Boolean,
        xpRate: Int,
        uid: String,
        world: World?,
        skillsJson: Any // of JsonArray
    ): JsonObject {
        val pObj = JsonObject()
        pObj.addProperty("username", username)
        pObj.addProperty("privilege", privilege)
        pObj.addProperty("gameMode", gameMode)
        pObj.addProperty("combatLvl", combatLvl)
        pObj.addProperty("isOnline", isOnline)
        pObj.addProperty("xpRate", xpRate)
        pObj.addProperty("UID", uid)

        // Skills
        val arr = JsonArray()
        if (world != null) {
            // online: gebruik world.getSkillNames en getSkills()
            for (i in 0 until world.maxSkills) {
                val skillLevel = /* haal currentLevel uit world */
                val skillName = /* Skills.getSkillName(world, world.getPlayerSkillId(...)) */
                val skObj = JsonObject().apply {
                    addProperty("name", skillName)
                    addProperty("currentLevel", skillLevel)
                }
                arr.add(skObj)
            }
        } else if (skillsJson is JsonArray) {
            // offline: skillsJson uit file
            skillsJson.forEach { elem ->
                val sk = elem.asJsonObject
                val skillId = sk.get("skill").asInt
                val skillName = Skills.getSkillName(world!!, skillId) // of lookup offline
                val skObj = JsonObject().apply {
                    addProperty("name", skillName)
                    addProperty("currentLevel", sk.get("lvl").asInt)
                }
                arr.add(skObj)
            }
        }
        pObj.add("skills", arr)
        return pObj
    }

    /** Zelfde resolveSavesDir als in HighScoresController */
    private fun resolveSavesDir(): Path? {
        val candidates = listOf(
            Paths.get("data","saves"),
            Paths.get("..","data","saves"),
            Paths.get("..","..","data","saves")
        )
        return candidates.firstOrNull { Files.isDirectory(it) }
    }
}
