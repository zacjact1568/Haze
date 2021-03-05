package net.zackzhang.code.haze.event

class CityDeletedEvent(eventSource: String, cityId: String, position: Int) : BaseEvent(eventSource, cityId, position)
