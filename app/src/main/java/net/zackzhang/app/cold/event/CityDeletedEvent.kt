package net.zackzhang.app.cold.event

class CityDeletedEvent(eventSource: String, cityId: String, position: Int) : BaseEvent(eventSource, cityId, position)
