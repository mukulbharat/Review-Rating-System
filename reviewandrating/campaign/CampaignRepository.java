package com.krenai.reviewandrating.campaign;

import com.krenai.reviewandrating.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign,Long> {
}
