package wumf.com.appsprovider2

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output

class GPAppSerializer : Serializer<GooglePlayApp>() {

    override fun write(kryo: Kryo?, output: Output?, gpApp: GooglePlayApp?) {
        output?.writeString(gpApp?.name)
        output?.writeString(gpApp?.iconUrl)
    }

    override fun read(kryo: Kryo?, input: Input?, type: Class<GooglePlayApp>?): GooglePlayApp {
        val name = input?.readString()
        val iconUrl = input?.readString()
        return GooglePlayApp("", iconUrl, name)
    }

}