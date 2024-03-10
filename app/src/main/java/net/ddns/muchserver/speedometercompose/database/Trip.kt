package net.ddns.muchserver.speedometercompose.database

class Trip {
    var id: Int = 0
    lateinit var checkPoint: CheckPoint

    constructor()

    constructor(checkPoint: CheckPoint) {
        this.checkPoint = checkPoint
    }
}