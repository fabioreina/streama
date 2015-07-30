package streama

import grails.transaction.Transactional

@Transactional
class DefaultDataService {

  def createDefaultRoles(){
    if (Role.count > 0) {
      return;
    }

    new Role(authority:"ROLE_ADMIN").save(failOnError: true)
  }


  def createDefaultUsers(){
    def users = [
        [
            username: 'admin',
            password: 'admin',
            enabled: true,
            role: Role.findByAuthority("ROLE_ADMIN")
        ]
    ]
    
    users.each{ userData ->
      if(!User.findByUsername(userData.username)){
        def user = new User(username: userData.username, password: userData.password, enabled: userData.enabled)
        user.save flush: true, failOnError: true

        UserRole.create(user, userData.role, true)
      }
    }
  }


  def createDefaultSettings(){
    def settings = [
        [
            settingsKey: 'Upload Directory',
            value: '/data/streama',
            description: 'This setting provides the application with your desired upload-path for all files.',
        ],
        [
            settingsKey: 'TheMovieDB API key',
            description: 'This API-key is required by the application to fetch all the nice Movie/Episode/Show data for you. Get one for free at https://www.themoviedb.org/',
        ],
    ]

    settings.each{ settingData ->
      if(!Settings.findBySettingsKey(settingData.settingsKey)){
        def setting = new Settings(settingData)
        setting.save flush: true, failOnError: true
      }
    }
  }
}
