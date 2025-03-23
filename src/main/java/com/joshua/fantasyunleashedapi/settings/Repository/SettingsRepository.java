package com.joshua.fantasyunleashedapi.settings.Repository;

import com.joshua.fantasyunleashedapi.settings.Model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {
}
