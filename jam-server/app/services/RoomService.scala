package services

import javax.inject.{Inject, Singleton}

import domain.models.{RoomName, UserName}

@Singleton
class RoomService @Inject() (roomRepository: RoomRepository) {
  def start(roomName: RoomName, userName: UserName): RoomInfo =
    roomRepository.room(roomName, userName)
}
