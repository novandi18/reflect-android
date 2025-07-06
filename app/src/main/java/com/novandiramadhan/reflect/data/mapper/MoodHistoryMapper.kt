package com.novandiramadhan.reflect.data.mapper

import com.novandiramadhan.reflect.data.local.room.entity.MoodHistoryEntity
import com.novandiramadhan.reflect.domain.model.Journal
import java.util.Date

object MoodHistoryMapper {
    fun MoodHistoryEntity.toJournal(): Journal =
        Journal(
            mood = mood,
            moodLevel = moodLevel,
            triggers = triggers,
            tags = tags,
            note = note,
            createdAt = com.google.firebase.Timestamp(Date(createdAt))
        )
}