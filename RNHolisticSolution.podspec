Pod::Spec.new do |s|
  package = JSON.parse(File.read(File.join(__dir__, "package.json")))

  s.name         = "RNHolisticSolution"
  s.version      = package['version']
  s.summary      = "React Native plugin for Holistic Solution SDK"
  s.description  = <<-DESC
                  React Native plugin for Holistic Solution SDK. It uses Appodeal, Firebase, AppsFlyer and Facebook SDK
                   DESC
  s.homepage     = "https://appodeal.com"
  s.license      = "MIT"
  s.author       = { "author" => "appodeal.com" }
  s.platform     = :ios, "9.0"
  s.source       = { :git => package['repository']['url'], :tag => "master" }
  s.source_files = "ios/**/*.{h,m}"
  
  s.requires_arc = true
  s.static_framework = true

  s.dependency "React"
  s.dependency "HolisticSolutionSDK", '~> 1.0.0'
end

  