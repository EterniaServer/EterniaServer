package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.eterniaserver.modules.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class KitService {

        private final List<String> kitNames = new ArrayList<>();
        private final Map<String, Utils.CustomKit> kitList = new HashMap<>();

        public List<String> kitNames() {
            return kitNames;
        }

        public Map<String, Utils.CustomKit> kitList() {
            return kitList;
        }

    }

}
